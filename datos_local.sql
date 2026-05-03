--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.usuario (id, nombre, correo, contrasena, rol, activo) VALUES (1, 'santiago', 'santiagoberriolopez@gmail.com', '$2a$12$r7X.ioUpgkW7w1Zfw7/RuuVErqcY0y4ra/nFLJ8VTFPtAFKWoIJay', 'ADMINISTRADOR', true);
INSERT INTO public.usuario (id, nombre, correo, contrasena, rol, activo) VALUES (2, 'sebastian', 'sebastianberrio97@gmail.com', '$2a$10$2lgV4ruWKrv/k/AF7y3yh.mJhm1ruDt/zHbSlt37JCSFha4noMrlu', 'CLIENTE', true);
INSERT INTO public.usuario (id, nombre, correo, contrasena, rol, activo) VALUES (6, 'santiago lopez', 'sberriol@unal.edu.co', '$2a$10$5ZhKDvelhbNjFQggg2qpA.Q4i9rhjySbL4ZzS4/TMLuGGQoq4J6ma', 'CLIENTE', true);
INSERT INTO public.usuario (id, nombre, correo, contrasena, rol, activo) VALUES (8, 'Santiago berrio lopez', 'sabelo@gmail.com', '$2a$10$1uHnjgB.RO2TqG1Ksd3PMOxk65qg059S/nCn1YQ9Fkm8wnjW0TloS', 'CLIENTE', true);
INSERT INTO public.usuario (id, nombre, correo, contrasena, rol, activo) VALUES (9, 'lucelly lopez', 'lc@gmail.com', '$2a$10$LBzhKG2HowfNivXXPqRYyeazVuAIPDHuvdHhZKKhhZB.1IAc7iPLO', 'CLIENTE', true);
INSERT INTO public.usuario (id, nombre, correo, contrasena, rol, activo) VALUES (10, 'sebastian berrio', 'sb@gmail.com', '$2a$10$xo/g8gKgGQhLmYXJ9HkTfugmXafz3pNo.A/pFcLaotH6TfRjR6Mn.', 'CLIENTE', true);
INSERT INTO public.usuario (id, nombre, correo, contrasena, rol, activo) VALUES (11, 'sebastian berrio', 'sebelo@gmail.com', '$2a$10$nc.pRwqLPy86qeiUa1XxJ.3s3cfGVyDNtjLiy0nQezWx6CrlN1ROi', 'CLIENTE', true);
INSERT INTO public.usuario (id, nombre, correo, contrasena, rol, activo) VALUES (12, 'Camila Uribe', 'cami95@gmail.com', '$2a$10$g6K0kWQJDWDSvo3l8x6XoOzURBiB89xObQzjKL4n.YmhbmE0snKxK', 'CLIENTE', true);
INSERT INTO public.usuario (id, nombre, correo, contrasena, rol, activo) VALUES (13, 'santiago berrio lopez', 'sasebecrypto@gmail.com', '$2a$10$u0E.mOW6nwSoO/bOZZcH3esPSL..nBvf/Ji9PeqIeV/jsPQHzlCoy', 'CLIENTE', true);


--
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.cliente (id, nombre, nit, ciudad, pais, usuario_id) VALUES (1, 'Santiago berrio lopez', '1036954912', 'medellin', 'colombia', 8);
INSERT INTO public.cliente (id, nombre, nit, ciudad, pais, usuario_id) VALUES (2, 'lucelly lopez', '43732151', 'buga', 'china', 9);
INSERT INTO public.cliente (id, nombre, nit, ciudad, pais, usuario_id) VALUES (3, 'sebastian berrio', '0000000', 'Medellin', 'Colombia', 11);
INSERT INTO public.cliente (id, nombre, nit, ciudad, pais, usuario_id) VALUES (4, 'Camila Uribe', '123456789', 'Medellin', 'Colombia', 12);
INSERT INTO public.cliente (id, nombre, nit, ciudad, pais, usuario_id) VALUES (5, 'santiago berrio lopez', '000000', 'Marinilla', 'Colombia', 13);


--
-- Data for Name: carrito; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (18, 2, '2026-02-24 20:19:21.065967', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (19, 3, '2026-02-24 20:55:15.370554', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (20, 3, '2026-03-28 12:11:29.243926', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (21, 3, '2026-03-28 12:20:12.062256', true);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (22, 3, '2026-04-12 12:47:47.265207', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (23, 3, '2026-04-12 12:49:11.009295', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (24, 1, '2026-04-12 12:58:51.354855', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (25, 1, '2026-04-12 13:41:47.350878', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (26, 1, '2026-04-12 17:48:19.485185', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (27, 1, '2026-04-12 17:58:55.924368', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (28, 1, '2026-04-12 18:33:49.018934', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (29, 1, '2026-04-12 18:56:54.595365', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (30, 1, '2026-04-13 19:44:10.664796', true);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (32, 4, '2026-04-14 21:45:43.898198', true);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (31, 3, '2026-04-14 21:46:42.968541', true);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (33, 5, '2026-04-14 21:56:37.131821', false);
INSERT INTO public.carrito (id, cliente_id, fecha_actualizacion, abandonado) VALUES (34, 4, '2026-04-20 20:55:17.99999', true);


--
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.categoria (id, nombre, descripcion) VALUES (1, 'Proteínas', 'Proteínas en polvo y suplementos proteicos');
INSERT INTO public.categoria (id, nombre, descripcion) VALUES (2, 'Aminoácidos', 'Aminoácidos esenciales, BCAA y fórmulas energéticas');
INSERT INTO public.categoria (id, nombre, descripcion) VALUES (3, 'Creatina', 'Creatinas monohidratadas y micronizadas');
INSERT INTO public.categoria (id, nombre, descripcion) VALUES (4, 'Pre entreno', 'Suplementos para mejorar energía y rendimiento');
INSERT INTO public.categoria (id, nombre, descripcion) VALUES (5, 'Vitaminas y Minerales', 'Vitaminas, minerales y suplementos de salud');
INSERT INTO public.categoria (id, nombre, descripcion) VALUES (6, 'Quemadores de grasa', 'Productos para definición y quema de grasa');
INSERT INTO public.categoria (id, nombre, descripcion) VALUES (7, 'Ganadores de peso', 'Gainers y suplementos para aumento de masa');
INSERT INTO public.categoria (id, nombre, descripcion) VALUES (8, 'Otros suplementos', 'Suplementos varios no clasificados');


--
-- Data for Name: marca; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.marca (id, nombre) VALUES (1, 'MuscleTech');
INSERT INTO public.marca (id, nombre) VALUES (2, 'Dymatize');
INSERT INTO public.marca (id, nombre) VALUES (3, 'Optimum Nutrition');
INSERT INTO public.marca (id, nombre) VALUES (4, 'Cellucor');
INSERT INTO public.marca (id, nombre) VALUES (5, 'AllMax');
INSERT INTO public.marca (id, nombre) VALUES (6, 'Himalaya');
INSERT INTO public.marca (id, nombre) VALUES (7, 'IMN');
INSERT INTO public.marca (id, nombre) VALUES (8, 'Universal Nutrition');
INSERT INTO public.marca (id, nombre) VALUES (9, 'ValeY');
INSERT INTO public.marca (id, nombre) VALUES (10, 'BPI Sports');
INSERT INTO public.marca (id, nombre) VALUES (11, 'Black Skull');
INSERT INTO public.marca (id, nombre) VALUES (13, 'Smart Nutrition');
INSERT INTO public.marca (id, nombre) VALUES (12, 'Nutramerican');
INSERT INTO public.marca (id, nombre) VALUES (14, 'MuscleMeds');
INSERT INTO public.marca (id, nombre) VALUES (15, 'RC labolatorios');
INSERT INTO public.marca (id, nombre) VALUES (16, 'BSN');
INSERT INTO public.marca (id, nombre) VALUES (17, 'ProScience');
INSERT INTO public.marca (id, nombre) VALUES (18, 'Smart Muscle');
INSERT INTO public.marca (id, nombre) VALUES (19, 'Otro');
INSERT INTO public.marca (id, nombre) VALUES (20, 'Dragon Pharma');
INSERT INTO public.marca (id, nombre) VALUES (21, 'NeoPharma');
INSERT INTO public.marca (id, nombre) VALUES (22, 'Basic');
INSERT INTO public.marca (id, nombre) VALUES (23, 'ElitePharma');
INSERT INTO public.marca (id, nombre) VALUES (24, 'RX');
INSERT INTO public.marca (id, nombre) VALUES (26, 'prueba1');


--
-- Data for Name: producto; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (55, 'NITROTECH 2LB', NULL, 0, true, 1, 1, 190000, 155000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (91, 'DRAGON PHARMA WHEY GOLD 2 LB', 'PROTEINA AISLADA', 3, true, 1, 20, 300000, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (51, 'WHEY GOLD 5 LB', '1', 9, true, 1, 3, 370000, 341000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (50, 'WHEY GOLD 2LB', '1', 10, true, 1, 3, 210000, 173000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (53, 'ISO 100 3LB', NULL, 10, true, 1, 2, 335000, 290000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (52, 'ISO 100 1.3LB', NULL, 4, true, 1, 2, 205000, 164000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (97, 'prueba', 'prueba', 7, true, 2, 13, 1, 1, '579b6755-1584-40bb-8389-c2a560764743_Gold_Standard_1_5_LB_Chocolate_Peanut_Butter_tarro(1).webp', '57e9d3e3-2983-44e6-94ad-acb8290cb22a_creatineplatinum.webp', 'chocolate', '2 lb');
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (93, 'WHEY GOLD 2LB prueba', 'prueba', 8, true, 1, 10, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (54, 'ISO 100 5LB', NULL, 2, true, 1, 2, 445000, 399000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (56, 'NITROTECH 4LB', NULL, 1, true, 1, 1, 300000, 255000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (57, 'NITROTECH 5LB', NULL, 1, true, 1, 1, 325000, 290000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (58, 'PLATINUM 80 SERV', NULL, 5, true, 1, 1, 150000, 130000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (65, 'C4 30 SERV CELLUCOR', NULL, 2, true, 4, 4, 130000, 100000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (66, 'C4 50 SERV CELLUCOR', NULL, 2, true, 4, 4, 170000, 140000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (74, 'CREATINA 150GR IMN 50 SERV', NULL, 1, true, 3, 7, 65000, 34300, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (75, 'CREATINA ON 120 SERVICIOS', NULL, 0, true, 3, 3, 175000, 144000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (76, 'CREATINA IMN 400G', NULL, 0, true, 3, 7, 110000, 92000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (77, 'ASHWAGANDHA HIMALAYA 60CAPS', NULL, 2, true, 5, 6, 75000, 55000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (47, 'BIPRO CLASSIC 3LB', NULL, 3, true, 1, 12, 205000, 162000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (48, 'WHEY PURE 2LB', NULL, 3, true, 1, 13, 130000, 89600, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (49, 'WHEY PURE 5LB', NULL, 2, true, 1, 13, 210000, 181760, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (59, 'CARNIVOR 4LB CHOCOLATE', NULL, 1, true, 1, 14, 75000, 247000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (60, 'AMINO TONE', NULL, 1, true, 2, 15, 145000, 100000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (61, 'AMINO ENERGY 30S', NULL, 1, true, 2, 3, 128000, 88000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (62, 'AMINO ENERGY 65S', NULL, 1, true, 2, 3, 205000, 169000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (63, 'AMINO X 30 SERV', NULL, 2, true, 2, 16, 120000, 88000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (64, 'AMINO X 70 SERV', NULL, 2, true, 2, 16, 205000, 169000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (67, 'INTENZE FRUIT PUNCH 30 SERV', NULL, 1, true, 4, 17, 135000, 101500, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (68, 'ELECTRON AZUL 30 SERV', NULL, 2, true, 4, 18, 115000, 87500, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (69, 'MANIAC PRE SANDIA', NULL, 1, true, 4, 19, 160000, 130000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (70, 'JOKER PRE MANGO', NULL, 0, true, 4, 19, 160000, 127000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (71, 'VENOM PRE ENTRENO', NULL, 1, true, 4, 20, 160000, 124000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (72, 'TNT 3LB VAINILLA', NULL, 1, true, 4, 21, 80000, 58100, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (73, 'TNT 10 LB', NULL, 0, true, 4, 21, 210000, 190000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (78, 'ASHWAGANDHA KSM-66 ALLMAX', NULL, 0, true, 5, 5, 80000, 58000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (79, 'GLICINATO MAGNESIO VALEY 100CAPS', NULL, 2, true, 5, 9, 80000, 48000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (83, 'CAFFEINA MUSCLETECH 125 TABS', NULL, 1, true, 5, 1, 80000, 50000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (46, 'BIPRO CLASSIC 2LB', NULL, 3, true, 1, 12, 150000, 118000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (80, 'MULTIVITAMINICO MUSCLE', NULL, 0, true, 5, 1, 90000, 60000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (81, 'GLUTAMINA BASIC', NULL, 1, true, 5, 22, 95000, 70000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (82, 'CAFFEINA', NULL, 1, true, 5, 19, 70000, 40000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (84, 'BURNER STACK 60 SERV', NULL, 1, true, 6, 12, 135000, 98000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (85, 'LIPOCORE ADVANCE', NULL, 0, true, 6, 23, 150000, 107000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (86, 'YOHIMBINA RX 100CAPS', NULL, 1, true, 6, 24, 95000, 50000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (87, 'MEGAPLEX 2LB VAINILLA', NULL, 2, true, 7, 12, 65000, 45500, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (88, 'PROTON GAINER 3LB VAINILLA', NULL, 1, true, 7, 18, 80000, 59500, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (89, 'SMART GAINER 3LB VAINILLA', NULL, 1, true, 7, 17, 90000, 69300, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (90, 'PROTON MAX 6LB', NULL, 0, true, 7, 18, 140000, 112000, NULL, NULL, NULL, NULL);
INSERT INTO public.producto (id, nombre, descripcion, stock, activo, categoria_id, marca_id, precio, costo, imagen_general, imagen_nutricional, sabor, tamano) VALUES (96, 'prueba', 'prueba', 10, true, 2, 13, 1, 1, '579b6755-1584-40bb-8389-c2a560764743_Gold_Standard_1_5_LB_Chocolate_Peanut_Butter_tarro(1).webp', '57e9d3e3-2983-44e6-94ad-acb8290cb22a_creatineplatinum.webp', 'vainilla', '5 Lb');


--
-- Data for Name: carrito_producto; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (86, 18, 97, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (88, 19, 97, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (90, 20, 51, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (91, 20, 97, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (92, 21, 97, 2, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (93, 22, 52, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (94, 23, 52, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (95, 24, 52, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (96, 25, 52, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (97, 26, 91, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (98, 27, 93, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (99, 28, 52, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (100, 29, 91, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (103, 30, 52, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (107, 32, 91, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (108, 31, 91, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (109, 31, 93, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (115, 33, 55, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (116, 33, 91, 1, NULL);
INSERT INTO public.carrito_producto (id, carrito_id, producto_id, cantidad, marca) VALUES (122, 34, 91, 2, NULL);


--
-- Data for Name: cupon; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.cupon (id, codigo, descuento_porcentaje, descuento_fijo, activo, fecha_expiracion, uso_maximo, uso_actual, monto_minimo) VALUES (1, 'DESCUENTO10', 10, NULL, true, '2027-12-31', 100, 2, 50000);


--
-- Data for Name: proveedor; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: orden_abastecimiento; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: detalle_abastecimiento; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: pedido; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (2, 2, '2026-02-24 20:29:31.718079', 'CREADO', 1, '2026-03-03 20:29:31.718079', NULL, 'Calle 40#81-29, El Retiro, Antioquia, Colombia', NULL);
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (3, 3, '2026-02-24 20:56:04.904307', 'CREADO', 1, '2026-03-03 20:56:04.904307', NULL, 'Calle 40#81-29, Marinilla, Antioquia, Colombia', NULL);
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (4, 3, '2026-03-28 12:11:55.707662', 'CREADO', 370001, '2026-04-04 12:11:55.707662', NULL, 'Calle 40#81-29, Rionegro, Antioquia, Colombia', 'CONTRA_ENTREGA');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (5, 3, '2026-04-12 12:48:04.679342', 'CREADO', 205000, '2026-04-19 12:48:04.679342', NULL, 'Marinilla calle 21 # 32-17, Rionegro, Antioquia, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (6, 3, '2026-04-12 12:49:31.64644', 'CREADO', 205000, '2026-04-19 12:49:31.64644', NULL, 'Marinilla calle 21 # 32-17, Rionegro, Antioquia, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (7, 3, '2026-04-12 12:52:18.7857', 'CREADO', 205000, '2026-04-19 12:52:18.7857', NULL, 'Marinilla calle 21 # 32-17, Rionegro, Antioquia, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (8, 1, '2026-04-12 12:59:04.778201', 'CREADO', 205000, '2026-04-19 12:59:04.778201', NULL, 'Marinilla calle 21 # 32-17, Rionegro, Antioquia, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (9, 1, '2026-04-12 13:02:27.530284', 'CREADO', 205000, '2026-04-19 13:02:27.530284', NULL, 'Marinilla calle 21 # 32-17, Rionegro, Antioquia, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (10, 1, '2026-04-12 13:05:09.612279', 'CREADO', 205000, '2026-04-19 13:05:09.612279', NULL, 'Marinilla calle 21 # 32-17, Leticia, Amazonas, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (11, 1, '2026-04-12 13:41:58.625686', 'CREADO', 205000, '2026-04-19 13:41:58.625686', NULL, 'Marinilla calle 21 # 32-17, La Unión, Antioquia, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (12, 1, '2026-04-12 17:48:49.09144', 'CREADO', 300000, '2026-04-19 17:48:49.09144', NULL, 'Marinilla calle 21 # 32-17, Rionegro, Antioquia, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (13, 1, '2026-04-12 17:59:07.435284', 'CREADO', 1, '2026-04-19 17:59:07.435284', NULL, 'Marinilla calle 21 # 32-17, Rionegro, Antioquia, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (14, 1, '2026-04-12 18:02:33.797122', 'CREADO', 1, '2026-04-19 18:02:33.798122', NULL, 'Marinilla calle 21 # 32-17, Alejandría, Antioquia, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (15, 1, '2026-04-12 18:21:37.804333', 'CREADO', 1, '2026-04-19 18:21:37.804333', NULL, 'Marinilla calle 21 # 32-17, Puerto Nariño, Amazonas, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (16, 1, '2026-04-12 18:29:54.510412', 'CREADO', 1, '2026-04-19 18:29:54.510412', NULL, 'Marinilla calle 21 # 32-17, Argelia, Antioquia, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (17, 1, '2026-04-12 18:32:36.378344', 'CREADO', 1, '2026-04-19 18:32:36.378344', NULL, 'Marinilla calle 21 # 32-17, Copacabana, Antioquia, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (18, 1, '2026-04-12 18:34:02.768794', 'CREADO', 205000, '2026-04-19 18:34:02.768794', NULL, 'Marinilla calle 21 # 32-17, Puerto Nariño, Amazonas, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (19, 1, '2026-04-12 18:57:06.747082', 'CREADO', 300000, '2026-04-19 18:57:06.747082', NULL, 'Marinilla calle 21 # 32-17, Aguachica, Cesar, Colombia', 'WOMPI');
INSERT INTO public.pedido (id, cliente_id, fecha_pedido, estado, total, fecha_entrega_estimado, fecha_entrega_real, direccion_envio, metodo_pago) VALUES (20, 5, '2026-04-14 21:56:56.967604', 'EN_PROCESO', 490000, '2026-04-21 21:56:56.967604', NULL, 'Marinilla calle 21 # 32-17, Marinilla, Antioquia, Colombia', 'WOMPI');


--
-- Data for Name: sabor; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (1, 'Vainilla', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (2, 'Chocolate', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (3, 'Fresa', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (4, 'Cookies and Cream', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (5, 'Gourmet Chocolate', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (6, 'Gourmet Vainilla', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (7, 'Fruity Pebbles', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (8, 'Birthday Cake', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (9, 'Milk Chocolate', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (10, 'Fruit Punch', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (11, 'Limonada Rosa', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (12, 'Blue Raspberry', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (13, 'Watermelon', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (14, 'Mango', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (15, 'Orange', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (16, 'Green Apple', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (17, 'Orange Burst', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (18, 'Sandia', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');
INSERT INTO public.sabor (id, nombre, activo, created_at, updated_at) VALUES (19, 'SIN SABOR', true, '2026-02-15 13:28:02.81759', '2026-02-15 13:28:02.81759');


--
-- Data for Name: tamaño; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (1, '1.3 LB', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (2, '2 LB', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (3, '3 LB', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (4, '4 LB', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (5, '5 LB', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (6, '6 LB', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (7, '10 LB', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (8, '200 G', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (9, '400 G', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (10, '150 G', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (11, '30 SERV', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (12, '50 SERV', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (13, '60 SERV', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (14, '65 SERV', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (15, '70 SERV', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (16, '80 SERV', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (17, '100 SERV', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (18, '120 SERV', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (19, '125 TABS', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (20, '30 CAPS', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (21, '60 CAPS', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (22, '100 CAPS', true, '2026-02-15 13:51:24.689758', '2026-02-15 13:51:24.689758');
INSERT INTO public."tamaño" (id, nombre, activo, created_at, updated_at) VALUES (23, '500 G', true, '2026-02-15 17:22:06.761426', '2026-02-15 17:22:06.761426');


--
-- Data for Name: variante_producto; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.variante_producto (id, producto_id, sabor_id, tamano_id, stock, codigo_sku, precio_ajuste, activo, created_at, updated_at) VALUES (1, 51, 1, 1, 10, NULL, 0.00, true, '2026-02-15 18:27:03.894168', '2026-02-15 18:27:03.894168');


--
-- Data for Name: detalle_pedido; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (2, 2, 97, 1, 1, 1, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (3, 3, 97, 1, 1, 1, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (4, 4, 97, 1, 1, 1, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (5, 4, 51, 1, 370000, 370000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (6, 5, 52, 1, 205000, 205000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (7, 6, 52, 1, 205000, 205000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (8, 7, 52, 1, 205000, 205000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (9, 8, 52, 1, 205000, 205000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (10, 9, 52, 1, 205000, 205000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (11, 10, 52, 1, 205000, 205000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (12, 11, 52, 1, 205000, 205000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (13, 12, 91, 1, 300000, 300000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (14, 13, 93, 1, 1, 1, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (15, 14, 93, 1, 1, 1, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (16, 15, 93, 1, 1, 1, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (17, 16, 93, 1, 1, 1, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (18, 17, 93, 1, 1, 1, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (19, 18, 52, 1, 205000, 205000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (20, 19, 91, 1, 300000, 300000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (21, 20, 55, 1, 190000, 190000, NULL);
INSERT INTO public.detalle_pedido (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal, variante_id) VALUES (22, 20, 91, 1, 300000, 300000, NULL);


--
-- Data for Name: historial; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (1, 50, '2026-02-04 20:28:37.957579', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (2, 50, '2026-02-04 20:28:54.896186', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (3, 50, '2026-02-04 20:29:11.477621', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (4, 51, '2026-02-04 20:33:18.592075', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (5, 51, '2026-02-04 20:33:28.836955', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (6, 50, '2026-02-04 21:26:52.605947', 3);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (7, 50, '2026-02-04 21:27:56.749445', 3);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (8, 50, '2026-02-04 21:28:09.640419', 3);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (9, 51, '2026-02-04 21:34:53.458169', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (10, 51, '2026-02-04 21:35:06.352409', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (11, 51, '2026-02-04 21:35:17.142242', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (12, 50, '2026-02-04 21:35:31.490991', 3);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (13, 91, '2026-02-05 19:29:20.896165', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (15, 93, '2026-02-08 10:43:03.415653', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (18, 96, '2026-02-17 21:18:40.387769', 1);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (19, 97, '2026-02-22 18:15:15.54228', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (20, 96, '2026-02-23 19:24:10.097089', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (21, 50, '2026-02-23 19:25:54.228223', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (22, 93, '2026-02-23 19:26:13.881513', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (23, 96, '2026-02-23 19:38:53.096323', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (24, 96, '2026-02-23 20:52:35.812596', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (25, 96, '2026-02-23 20:55:19.014648', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (26, 52, '2026-02-23 21:17:12.723701', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (27, 53, '2026-02-23 21:17:12.731846', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (28, 51, '2026-02-24 18:59:46.918482', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (29, 97, '2026-02-24 19:02:09.332204', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (30, 97, '2026-02-24 19:12:06.111327', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (31, 97, '2026-02-24 19:12:06.138329', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (32, 97, '2026-02-24 19:12:29.417271', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (33, 97, '2026-02-24 19:12:29.432153', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (34, 50, '2026-02-24 19:12:29.435946', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (35, 97, '2026-02-24 19:12:53.279756', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (36, 50, '2026-02-24 19:12:53.286883', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (37, 97, '2026-02-24 19:12:53.305871', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (38, 50, '2026-02-24 19:12:53.311993', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (39, 97, '2026-02-24 19:12:58.999802', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (40, 50, '2026-02-24 19:12:59.005895', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (41, 97, '2026-02-24 19:12:59.031246', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (42, 50, '2026-02-24 19:12:59.034711', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (43, 53, '2026-02-24 19:12:59.038804', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (44, 97, '2026-02-24 19:13:13.32988', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (45, 50, '2026-02-24 19:13:13.334053', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (46, 53, '2026-02-24 19:13:13.339502', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (47, 97, '2026-02-24 19:13:13.361732', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (48, 50, '2026-02-24 19:13:13.365972', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (49, 53, '2026-02-24 19:13:13.370998', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (50, 97, '2026-02-24 19:13:40.249749', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (51, 50, '2026-02-24 19:13:40.253094', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (52, 53, '2026-02-24 19:13:40.256988', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (53, 50, '2026-02-24 19:13:40.265838', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (54, 53, '2026-02-24 19:13:40.268862', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (55, 50, '2026-02-24 19:14:22.889838', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (56, 53, '2026-02-24 19:14:22.893789', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (57, 50, '2026-02-24 19:14:22.905406', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (58, 53, '2026-02-24 19:14:22.909113', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (59, 50, '2026-02-24 19:14:42.060331', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (60, 53, '2026-02-24 19:14:42.063397', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (61, 96, '2026-02-24 19:14:42.075505', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (62, 50, '2026-02-24 19:14:42.078189', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (63, 53, '2026-02-24 19:14:42.081319', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (64, 96, '2026-02-24 19:14:47.450465', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (65, 50, '2026-02-24 19:14:47.45492', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (66, 53, '2026-02-24 19:14:47.458177', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (67, 50, '2026-02-24 19:14:47.467418', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (68, 53, '2026-02-24 19:14:47.470734', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (69, 50, '2026-02-24 19:14:57.515761', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (70, 53, '2026-02-24 19:14:57.519189', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (71, 50, '2026-02-24 19:14:57.532067', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (72, 53, '2026-02-24 19:14:57.536757', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (73, 50, '2026-02-24 19:15:08.381773', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (74, 53, '2026-02-24 19:15:08.384797', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (75, 96, '2026-02-24 19:15:08.396122', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (76, 50, '2026-02-24 19:15:08.398428', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (77, 53, '2026-02-24 19:15:08.401315', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (78, 96, '2026-02-24 19:15:16.361646', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (79, 50, '2026-02-24 19:15:16.367187', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (80, 53, '2026-02-24 19:15:16.370991', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (81, 50, '2026-02-24 19:15:16.392777', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (82, 53, '2026-02-24 19:15:16.395463', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (83, 50, '2026-02-24 19:15:29.030824', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (84, 53, '2026-02-24 19:15:29.034816', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (85, 50, '2026-02-24 19:15:29.045665', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (86, 53, '2026-02-24 19:15:29.048057', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (87, 50, '2026-02-24 19:15:41.529853', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (88, 53, '2026-02-24 19:15:41.533977', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (89, 96, '2026-02-24 19:15:41.548544', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (90, 50, '2026-02-24 19:15:41.552608', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (91, 53, '2026-02-24 19:15:41.555642', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (92, 96, '2026-02-24 19:15:50.998812', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (93, 50, '2026-02-24 19:15:51.00255', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (94, 53, '2026-02-24 19:15:51.005607', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (95, 50, '2026-02-24 19:15:51.014602', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (96, 53, '2026-02-24 19:15:51.01733', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (97, 50, '2026-02-24 19:16:11.250929', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (98, 53, '2026-02-24 19:16:11.25391', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (99, 97, '2026-02-24 19:16:11.272537', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (100, 50, '2026-02-24 19:16:11.277903', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (101, 53, '2026-02-24 19:16:11.282663', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (102, 97, '2026-02-24 19:16:23.89229', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (103, 50, '2026-02-24 19:16:23.895212', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (104, 53, '2026-02-24 19:16:23.897734', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (105, 97, '2026-02-24 19:16:23.907579', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (106, 50, '2026-02-24 19:16:23.910712', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (107, 53, '2026-02-24 19:16:23.913587', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (108, 97, '2026-02-24 19:18:10.371977', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (109, 50, '2026-02-24 19:18:10.37608', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (110, 53, '2026-02-24 19:18:10.37848', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (111, 97, '2026-02-24 19:18:10.388354', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (112, 50, '2026-02-24 19:18:10.39119', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (113, 53, '2026-02-24 19:18:10.394514', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (114, 97, '2026-02-24 19:18:14.850036', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (115, 50, '2026-02-24 19:18:14.853845', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (116, 53, '2026-02-24 19:18:14.857252', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (117, 50, '2026-02-24 19:18:14.866285', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (118, 53, '2026-02-24 19:18:14.869363', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (119, 50, '2026-02-24 19:18:20.978203', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (120, 53, '2026-02-24 19:18:20.982466', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (121, 50, '2026-02-24 19:18:20.994213', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (122, 53, '2026-02-24 19:18:20.997408', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (123, 50, '2026-02-24 19:18:30.340382', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (124, 53, '2026-02-24 19:18:30.343195', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (125, 96, '2026-02-24 19:18:30.354526', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (126, 50, '2026-02-24 19:18:30.35719', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (127, 53, '2026-02-24 19:18:30.35979', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (128, 96, '2026-02-24 19:18:36.192326', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (129, 50, '2026-02-24 19:18:36.197403', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (130, 53, '2026-02-24 19:18:36.200348', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (131, 50, '2026-02-24 19:18:36.207521', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (132, 53, '2026-02-24 19:18:36.210415', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (133, 50, '2026-02-24 19:18:42.338366', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (134, 53, '2026-02-24 19:18:42.34299', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (135, 50, '2026-02-24 19:18:42.3535', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (136, 53, '2026-02-24 19:18:42.356053', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (137, 50, '2026-02-24 19:18:47.353098', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (138, 53, '2026-02-24 19:18:47.357315', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (139, 97, '2026-02-24 19:18:47.368215', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (140, 50, '2026-02-24 19:18:47.371871', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (141, 53, '2026-02-24 19:18:47.374201', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (142, 97, '2026-02-24 19:19:05.487459', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (143, 50, '2026-02-24 19:19:05.502011', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (144, 53, '2026-02-24 19:19:05.506231', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (145, 97, '2026-02-24 19:19:05.517601', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (146, 50, '2026-02-24 19:19:05.521468', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (147, 53, '2026-02-24 19:19:05.524703', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (148, 97, '2026-02-24 19:19:29.3228', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (149, 50, '2026-02-24 19:19:29.32732', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (150, 53, '2026-02-24 19:19:29.329892', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (151, 97, '2026-02-24 19:40:06.548388', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (152, 97, '2026-02-24 19:40:10.935375', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (153, 97, '2026-02-24 19:41:14.343883', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (154, 97, '2026-02-24 19:41:14.358364', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (155, 97, '2026-02-24 19:41:15.324454', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (156, 97, '2026-02-24 19:41:15.342091', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (157, 96, '2026-02-24 19:44:46.135238', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (158, 97, '2026-02-24 19:45:25.067878', 6);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (159, 97, '2026-02-24 19:45:26.500876', 4);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (160, 97, '2026-02-24 19:46:33.227457', 3);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (161, 97, '2026-02-24 19:46:58.264799', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (162, 97, '2026-02-24 19:47:44.35337', 3);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (163, 97, '2026-02-24 19:47:44.367821', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (164, 97, '2026-02-24 19:48:12.341022', 3);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (165, 97, '2026-02-24 19:48:12.354982', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (166, 97, '2026-02-24 19:48:31.334554', 3);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (167, 97, '2026-02-24 19:48:31.349262', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (168, 97, '2026-02-24 19:53:45.640305', 3);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (169, 97, '2026-02-24 19:53:45.654915', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (170, 97, '2026-02-24 19:56:23.660092', 1);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (171, 97, '2026-02-24 19:57:00.53938', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (172, 97, '2026-02-24 19:57:00.555169', 1);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (173, 97, '2026-02-24 20:19:21.051946', 2);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (174, 97, '2026-02-24 20:19:21.064612', 1);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (175, 97, '2026-02-24 20:55:05.363079', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (176, 97, '2026-02-24 20:55:15.341793', 1);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (177, 97, '2026-02-24 20:55:15.367393', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (178, 97, '2026-03-28 12:11:25.048277', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (179, 97, '2026-03-28 12:11:29.21996', 10);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (180, 97, '2026-03-28 12:11:29.235233', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (181, 51, '2026-03-28 12:11:29.239604', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (182, 97, '2026-03-28 12:20:12.056621', 7);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (183, 52, '2026-04-12 12:47:47.173811', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (184, 52, '2026-04-12 12:49:11.005095', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (185, 52, '2026-04-12 12:58:51.276608', 7);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (186, 52, '2026-04-12 13:41:47.285085', 6);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (187, 91, '2026-04-12 17:48:19.476686', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (188, 93, '2026-04-12 17:58:55.917216', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (189, 52, '2026-04-12 18:33:49.011453', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (190, 91, '2026-04-12 18:56:54.589945', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (191, 52, '2026-04-13 19:39:03.558666', 4);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (192, 52, '2026-04-13 19:39:13.673204', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (193, 52, '2026-04-13 19:39:13.689331', 4);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (194, 52, '2026-04-13 19:44:10.646564', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (195, 52, '2026-04-13 19:44:10.661981', 4);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (196, 93, '2026-04-14 21:22:51.058452', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (197, 93, '2026-04-14 21:27:07.753988', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (198, 93, '2026-04-14 21:27:07.769857', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (199, 91, '2026-04-14 21:45:31.956596', 7);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (200, 91, '2026-04-14 21:45:43.87927', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (201, 91, '2026-04-14 21:45:43.894673', 7);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (202, 93, '2026-04-14 21:46:42.947209', 9);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (203, 91, '2026-04-14 21:46:42.962489', 6);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (204, 93, '2026-04-14 21:46:42.966011', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (205, 91, '2026-04-14 21:55:50.377081', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (206, 93, '2026-04-14 21:55:50.382473', 7);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (207, 91, '2026-04-14 21:55:58.129018', 6);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (208, 93, '2026-04-14 21:55:58.133035', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (209, 55, '2026-04-14 21:55:58.156932', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (210, 91, '2026-04-14 21:55:58.160813', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (211, 93, '2026-04-14 21:55:58.165135', 7);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (212, 55, '2026-04-14 21:56:37.096486', 1);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (213, 91, '2026-04-14 21:56:37.101167', 6);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (214, 93, '2026-04-14 21:56:37.10471', 8);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (215, 55, '2026-04-14 21:56:37.126874', 0);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (216, 91, '2026-04-14 21:56:37.129969', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (217, 91, '2026-04-20 19:52:03.837133', 4);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (218, 91, '2026-04-20 19:52:15.430373', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (219, 91, '2026-04-20 19:52:15.445376', 4);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (220, 91, '2026-04-20 19:52:27.982506', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (221, 91, '2026-04-20 19:52:27.998294', 4);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (222, 91, '2026-04-20 19:52:30.691753', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (223, 91, '2026-04-20 19:52:30.707611', 3);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (224, 91, '2026-04-20 20:15:33.500176', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (225, 91, '2026-04-20 20:15:33.526982', 3);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (226, 91, '2026-04-20 20:55:17.969485', 5);
INSERT INTO public.historial (id, producto_id, fecha_cambio, stock_cambiado) VALUES (227, 91, '2026-04-20 20:55:17.995671', 3);


--
-- Data for Name: notificacion; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: reporte_venta; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Name: carrito_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.carrito_id_seq', 34, true);


--
-- Name: carrito_producto_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.carrito_producto_id_seq', 122, true);


--
-- Name: categoria_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.categoria_id_seq', 10, true);


--
-- Name: cliente_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.cliente_id_seq', 5, true);


--
-- Name: cupon_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.cupon_id_seq', 1, true);


--
-- Name: detalle_abastecimiento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.detalle_abastecimiento_id_seq', 1, false);


--
-- Name: detalle_pedido_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.detalle_pedido_id_seq', 22, true);


--
-- Name: historial_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.historial_id_seq', 227, true);


--
-- Name: marca_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.marca_id_seq', 27, true);


--
-- Name: notificacion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.notificacion_id_seq', 1, false);


--
-- Name: orden_abastecimiento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.orden_abastecimiento_id_seq', 1, false);


--
-- Name: pedido_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.pedido_id_seq', 20, true);


--
-- Name: producto_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.producto_id_seq', 97, true);


--
-- Name: proveedor_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.proveedor_id_seq', 1, false);


--
-- Name: reporte_venta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.reporte_venta_id_seq', 1, false);


--
-- Name: sabor_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.sabor_id_seq', 19, true);


--
-- Name: tamaño_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public."tamaño_id_seq"', 23, true);


--
-- Name: usuario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.usuario_id_seq', 13, true);


--
-- Name: variante_producto_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.variante_producto_id_seq', 1, true);


--
-- PostgreSQL database dump complete
--

