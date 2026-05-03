BEGIN;

-- ============================================================
-- FASE 1: Sabores nuevos
-- ============================================================
INSERT INTO sabor (nombre, activo, created_at, updated_at)
SELECT 'Azul', true, NOW(), NOW() WHERE NOT EXISTS (SELECT 1 FROM sabor WHERE LOWER(nombre)='azul');

INSERT INTO sabor (nombre, activo, created_at, updated_at)
SELECT 'Amarillo', true, NOW(), NOW() WHERE NOT EXISTS (SELECT 1 FROM sabor WHERE LOWER(nombre)='amarillo');

-- ============================================================
-- FASE 2: Limpiar FKs antes de borrar productos
-- ============================================================
-- IDs a eliminar:
--   91  = DRAGON PHARMA WHEY GOLD 2 LB (no listado)
--   96  = prueba (no listado)
--   98  = RAW 2 LB (no listado)
--   93  = WHEY GOLD 2LB prueba (no listado)
--   51  = WHEY GOLD Vainilla (duplicado de id:50 después de update)
--  102  = BIPRO CLASSIC 2LB Chocolate (sabor no listado)
--   99  = BIPRO CLASSIC 3LB Chocolate (sabor no listado)

DELETE FROM historial       WHERE producto_id IN (91,96,98,93,51,102,99);
DELETE FROM carrito_producto WHERE producto_id IN (91,96,98,93,51,102,99);

-- ============================================================
-- FASE 3: Borrar productos
-- ============================================================
DELETE FROM producto WHERE id IN (91,96,98,93,51,102,99);

-- ============================================================
-- FASE 4: Actualizar productos existentes
-- ============================================================

-- BIPRO CLASSIC 2LB  (INV=3, 2 sabores → [2,1])
UPDATE producto SET precio=170000, costo=136500, stock=2 WHERE id=101; -- Vainilla
UPDATE producto SET precio=170000, costo=136500, stock=1 WHERE id=46;  -- Cookies and Cream

-- BIPRO CLASSIC 3LB  (INV=3, 2 sabores → [2,1])
UPDATE producto SET precio=225000, costo=185000, stock=2 WHERE id=100; -- Vainilla
UPDATE producto SET precio=225000, costo=185000, stock=1 WHERE id=47;  -- Cookies and Cream

-- WHEY PURE 2LB  →  Vainilla (INV=3, 2 sabores → stock=2)
UPDATE producto SET sabor='Vainilla', precio=155000, costo=112700, stock=2 WHERE id=48;

-- WHEY PURE 5LB  →  Vainilla (INV=2, 2 sabores → stock=1)
UPDATE producto SET sabor='Vainilla', precio=350000, costo=276000, stock=1 WHERE id=49;

-- WHEY GOLD 2LB: id:50 sin sabor → Vainilla; id:103 "WHEY GOLD" → rename
UPDATE producto SET sabor='Vainilla', precio=225000, costo=181000, stock=1, tamano='2 LB' WHERE id=50;
UPDATE producto SET nombre='WHEY GOLD 2LB', precio=225000, costo=181000, stock=1 WHERE id=103;

-- ISO 100 1.3LB  →  Cookies and Cream
UPDATE producto SET sabor='Cookies and Cream', precio=235000, costo=164000, stock=1 WHERE id=52;

-- ISO 100 3LB
UPDATE producto SET precio=360000, costo=290000, stock=0 WHERE id=53;

-- ISO 100 5LB  →  Vainilla (INV=2, 2 sabores → stock=1)
UPDATE producto SET sabor='Vainilla', precio=560000, costo=500000, stock=1 WHERE id=54;

-- NITROTECH 2LB
UPDATE producto SET sabor='Vainilla', precio=200000, costo=155000, stock=1 WHERE id=55;

-- NITROTECH 4LB
UPDATE producto SET sabor='Vainilla', precio=320000, costo=255000, stock=1 WHERE id=56;

-- NITROTECH 5LB
UPDATE producto SET precio=350000, costo=300000, stock=1 WHERE id=57;

-- PLATINUM 80 SERV
UPDATE producto SET precio=165000, costo=130000, stock=4 WHERE id=58;

-- AMINO ENERGY 65S
UPDATE producto SET precio=215000 WHERE id=62;

-- AMINO X 30 SERV  →  Fruit Punch (INV=2, 2 sabores → stock=1)
UPDATE producto SET sabor='Fruit Punch', precio=125000, costo=95000, stock=1 WHERE id=63;

-- AMINO X 70 SERV
UPDATE producto SET precio=220000, costo=185000 WHERE id=64;

-- C4 50 SERV CELLUCOR
UPDATE producto SET precio=175000 WHERE id=66;

-- CAFFEINA
UPDATE producto SET stock=2 WHERE id=82;

-- CAFFEINA MUSCLETECH 125 TABS
UPDATE producto SET stock=0 WHERE id=83;

-- CARNIVOR 4LB  (precio estaba en 75000, corregir a 295000!)
UPDATE producto SET nombre='CARNIVOR 4LB', sabor='Chocolate', precio=295000 WHERE id=59;

-- CREATINA 150 GR IMN 50 SERV 3GR
UPDATE producto SET nombre='CREATINA 150 GR IMN 50 SERV 3GR' WHERE id=74;

-- CREATINA IMN 400G
UPDATE producto SET costo=84000 WHERE id=76;

-- CREATINA ON 120 SERVICIOS MICRONIZADA
UPDATE producto SET nombre='CREATINA ON 120 SERVICIOS MICRONIZADA' WHERE id=75;

-- ELECTRON 30Serv  (era "ELECTRON AZUL 30 SERV")
UPDATE producto SET nombre='ELECTRON 30Serv', sabor='Azul' WHERE id=68;

-- GLICINATO MAGNESIO VALEY 100CAPS
UPDATE producto SET stock=1 WHERE id=79;

-- ASHWAGANDHA KSM-66 ALLMAX 60CAPS
UPDATE producto SET nombre='ASHWAGANDHA KSM-66 ALLMAX 60CAPS' WHERE id=78;

-- INTENZE 30 SER  (era "INTENZE FRUIT PUNCH 30 SERV")
UPDATE producto SET nombre='INTENZE 30 SER', sabor='Fruit Punch' WHERE id=67;

-- JOKER PRE  (era "JOKER PRE MANGO")
UPDATE producto SET nombre='JOKER PRE', sabor='Mango', stock=1 WHERE id=70;

-- MANIAC PRE  (era "MANIAC PRE SANDIA")
UPDATE producto SET nombre='MANIAC PRE', sabor='Sandia' WHERE id=69;

-- MEGAPLEX 2LB  (era "MEGAPLEX 2LB VAINILLA")
UPDATE producto SET nombre='MEGAPLEX 2LB', sabor='Vainilla' WHERE id=87;

-- MULTIVITAMINICO MUSCLE
UPDATE producto SET precio=100000, costo=65000, stock=1 WHERE id=80;

-- PROTON GAINER 3LB  (era "PROTON GAINER 3LB VAINILLA")
UPDATE producto SET nombre='PROTON GAINER 3LB', sabor='Vainilla' WHERE id=88;

-- SMART GAINER 3LB  (era "SMART GAINER 3LB VAINILLA")
UPDATE producto SET nombre='SMART GAINER 3LB', sabor='Vainilla' WHERE id=89;

-- TNT 3LB  (era "TNT 3LB VAINILLA")
UPDATE producto SET nombre='TNT 3LB', sabor='Vainilla', precio=86000, costo=64500 WHERE id=72;

-- TNT 10LB  (era "TNT 10 LB")
UPDATE producto SET nombre='TNT 10LB', sabor='Vainilla', precio=235000, costo=200000, stock=1 WHERE id=73;

-- VENOM PRE ENTRENO
UPDATE producto SET precio=164000, stock=0 WHERE id=71;

-- BURNER STACK 60SERV  (era "BURNER STACK 60 SERV")
UPDATE producto SET nombre='BURNER STACK 60SERV' WHERE id=84;

-- YOHIMBINA RX 100CAPS
UPDATE producto SET stock=0 WHERE id=86;

-- ============================================================
-- FASE 5: Crear variantes nuevas de productos ya existentes
-- ============================================================

-- WHEY PURE 2LB, Chocolate  (INV=3 → stock=1 restante)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('WHEY PURE 2LB','WHEY PURE 2LB','Chocolate','',155000,112700,1,1,13,true,NULL,NULL);

-- WHEY PURE 5LB, Fresa  (INV=2 → stock=1)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('WHEY PURE 5LB','WHEY PURE 5LB','Fresa','',350000,276000,1,1,13,true,NULL,NULL);

-- WHEY GOLD 5LB, Vainilla  (INV=2 → stock=1; cat:1, marca:3 Optimum Nutrition)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('WHEY GOLD 5LB','WHEY GOLD 5LB','Vainilla','5 LB',410000,341000,1,1,3,true,NULL,NULL);

-- WHEY GOLD 5LB, Chocolate  (INV=2 → stock=1)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('WHEY GOLD 5LB','WHEY GOLD 5LB','Chocolate','5 LB',410000,341000,1,1,3,true,NULL,NULL);

-- ISO 100 5LB, Cookies and Cream  (INV=2 → stock=1; cat:1, marca:2 Dymatize)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('ISO 100 5LB','ISO 100 5LB','Cookies and Cream','',560000,500000,1,1,2,true,NULL,NULL);

-- AMINO X 30 SERV, Watermelon  (INV=2 → stock=1; cat:2, marca:16 BSN)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('AMINO X 30 SERV','AMINO X 30 SERV','Watermelon','',125000,95000,1,2,16,true,NULL,NULL);

-- ============================================================
-- FASE 6: Crear productos completamente nuevos
-- ============================================================

-- IRON 100 SERV  (cat:1 Proteínas, marca:1 MuscleTech)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('IRON 100 SERV','IRON 100 SERV','','',115000,80000,4,1,1,true,NULL,NULL);

-- CAFEINA NUTRAKEY  (cat:5 Vitaminas y Minerales, marca:19 Otro)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('CAFEINA NUTRAKEY','CAFEINA NUTRAKEY','','',90000,55000,1,5,19,true,NULL,NULL);

-- CITRATO DE MAGNESIO  (cat:5, marca:19)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('CITRATO DE MAGNESIO','CITRATO DE MAGNESIO','','',70000,36000,1,5,19,true,NULL,NULL);

-- BETA ALANINA METABOLIC  (cat:2 Aminoácidos, marca:19)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('BETA ALANINA METABOLIC','BETA ALANINA METABOLIC','','',115000,84000,1,2,19,true,NULL,NULL);

-- CREATINA ON 60 SERVICIOS MICRONIZADA  (cat:3, marca:3 Optimum Nutrition)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('CREATINA ON 60 SERVICIOS MICRONIZADA','CREATINA ON 60 SERVICIOS MICRONIZADA','','',125000,101000,1,3,3,true,NULL,NULL);

-- NITROX 200G  (cat:4 Pre entreno, marca:19)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('NITROX 200G','NITROX 200G','','',50000,35000,2,4,19,true,NULL,NULL);

-- NITROFIT 30CAPS  (cat:6 Quemadores, marca:19)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('NITROFIT 30CAPS','NITROFIT 30CAPS','','',110000,85000,1,6,19,true,NULL,NULL);

-- HYDROXYCUT ON  (cat:6 Quemadores, marca:1 MuscleTech)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('HYDROXYCUT ON','HYDROXYCUT ON','','',112000,82000,1,6,1,true,NULL,NULL);

-- PASE PRE ENTRENO  (cat:4 Pre entreno, marca:19)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('PASE PRE ENTRENO','PASE PRE ENTRENO','','',100000,73500,0,4,19,true,NULL,NULL);

-- PANCAKES NUTRAMERICAN, Amarillo  (cat:8 Otros, marca:12 Nutramerican)
INSERT INTO producto (nombre, descripcion, sabor, tamano, precio, costo, stock, categoria_id, marca_id, activo, imagen_general, imagen_nutricional)
VALUES ('PANCAKES NUTRAMERICAN','PANCAKES NUTRAMERICAN','Amarillo','',45000,30000,1,8,12,true,NULL,NULL);

COMMIT;

-- Verificación final
SELECT COUNT(*) AS total_productos FROM producto;
SELECT nombre, sabor, precio, costo, stock FROM producto ORDER BY nombre, sabor;
