BEGIN;

-- AMINO ENERGY: strip size → tamano
UPDATE producto SET nombre='AMINO ENERGY', tamano='30 SERV' WHERE id=61;
UPDATE producto SET nombre='AMINO ENERGY', tamano='65 SERV' WHERE id=62;

-- AMINO X: strip size → tamano
UPDATE producto SET nombre='AMINO X', tamano='30 SERV' WHERE id IN (63,109);
UPDATE producto SET nombre='AMINO X', tamano='70 SERV' WHERE id=64;

-- ASHWAGANDHA: strip caps count → tamano
UPDATE producto SET nombre='ASHWAGANDHA HIMALAYA', tamano='60 CAPS' WHERE id=77;
UPDATE producto SET nombre='ASHWAGANDHA KSM-66 ALLMAX', tamano='60 CAPS' WHERE id=78;

-- BIPRO CLASSIC: already has tamano set, just strip from nombre
UPDATE producto SET nombre='BIPRO CLASSIC' WHERE id IN (46,101,47,100);

-- BURNER STACK: strip servings → tamano
UPDATE producto SET nombre='BURNER STACK', tamano='60 SERV' WHERE id=84;

-- C4 CELLUCOR: reorder + strip size → tamano
UPDATE producto SET nombre='C4 CELLUCOR', tamano='30 SERV' WHERE id=65;
UPDATE producto SET nombre='C4 CELLUCOR', tamano='50 SERV' WHERE id=66;

-- CAFFEINA MUSCLETECH: strip tabs → tamano
UPDATE producto SET nombre='CAFFEINA MUSCLETECH', tamano='125 TABS' WHERE id=83;

-- CARNIVOR: strip size → tamano
UPDATE producto SET nombre='CARNIVOR', tamano='4 LB' WHERE id=59;

-- CREATINA IMN: unify name, strip size → tamano
UPDATE producto SET nombre='CREATINA IMN', tamano='150 GR' WHERE id=74;
UPDATE producto SET nombre='CREATINA IMN', tamano='400 G' WHERE id=76;

-- CREATINA ON MICRONIZADA: strip serv count → tamano
UPDATE producto SET nombre='CREATINA ON MICRONIZADA', tamano='60 SERV' WHERE id=114;
UPDATE producto SET nombre='CREATINA ON MICRONIZADA', tamano='120 SERV' WHERE id=75;

-- ELECTRON: strip servings → tamano
UPDATE producto SET nombre='ELECTRON', tamano='30 SERV' WHERE id=68;

-- GLICINATO MAGNESIO VALEY: strip caps → tamano
UPDATE producto SET nombre='GLICINATO MAGNESIO VALEY', tamano='100 CAPS' WHERE id=79;

-- INTENZE: strip servings → tamano
UPDATE producto SET nombre='INTENZE', tamano='30 SERV' WHERE id=67;

-- IRON: strip servings → tamano + corrección categoría → Creatina
UPDATE producto SET nombre='IRON', tamano='100 SERV', categoria_id=3 WHERE id=110;

-- ISO 100: strip size → tamano
UPDATE producto SET nombre='ISO 100', tamano='1.3 LB' WHERE id=52;
UPDATE producto SET nombre='ISO 100', tamano='3 LB' WHERE id=53;
UPDATE producto SET nombre='ISO 100', tamano='5 LB' WHERE id IN (54,108);

-- MEGAPLEX: strip size → tamano
UPDATE producto SET nombre='MEGAPLEX', tamano='2 LB' WHERE id=87;

-- NITROFIT: strip caps → tamano
UPDATE producto SET nombre='NITROFIT', tamano='30 CAPS' WHERE id=116;

-- NITROTECH: strip size → tamano
UPDATE producto SET nombre='NITROTECH', tamano='2 LB' WHERE id=55;
UPDATE producto SET nombre='NITROTECH', tamano='4 LB' WHERE id=56;
UPDATE producto SET nombre='NITROTECH', tamano='5 LB' WHERE id=57;

-- NITROX: strip weight → tamano
UPDATE producto SET nombre='NITROX', tamano='200 G' WHERE id=115;

-- PLATINUM: strip servings → tamano + corrección categoría → Creatina (MuscleTech)
UPDATE producto SET nombre='PLATINUM', tamano='80 SERV', categoria_id=3 WHERE id=58;

-- PROTON GAINER: strip size → tamano
UPDATE producto SET nombre='PROTON GAINER', tamano='3 LB' WHERE id=88;

-- PROTON MAX: strip size → tamano
UPDATE producto SET nombre='PROTON MAX', tamano='6 LB' WHERE id=90;

-- SMART GAINER: strip size → tamano
UPDATE producto SET nombre='SMART GAINER', tamano='3 LB' WHERE id=89;

-- TNT: strip size → tamano
UPDATE producto SET nombre='TNT', tamano='3 LB' WHERE id=72;
UPDATE producto SET nombre='TNT', tamano='10 LB' WHERE id=73;

-- WHEY GOLD: already has tamano set, just strip from nombre
UPDATE producto SET nombre='WHEY GOLD' WHERE id IN (50,103,106,107);

-- WHEY PURE: strip size → tamano
UPDATE producto SET nombre='WHEY PURE', tamano='2 LB' WHERE id IN (48,104);
UPDATE producto SET nombre='WHEY PURE', tamano='5 LB' WHERE id IN (49,105);

-- YOHIMBINA RX: strip caps → tamano
UPDATE producto SET nombre='YOHIMBINA RX', tamano='100 CAPS' WHERE id=86;

COMMIT;

-- Verificación
\pset pager off
SELECT id, nombre, sabor, tamano, categoria_id FROM producto ORDER BY nombre, tamano, sabor;
