#!/usr/bin/env node
/**
 * Bulk update de productos desde spreadsheet de inventario.
 * Uso: ADMIN_EMAIL=x ADMIN_PASSWORD=y node scripts/bulk-update-productos.mjs [--dry-run]
 */

const DRY_RUN = process.argv.includes('--dry-run');
const API_BASE = process.env.API_BASE || 'https://olympo-gateway.fly.dev';
const ADMIN_EMAIL = process.env.ADMIN_EMAIL || '';
const ADMIN_PASSWORD = process.env.ADMIN_PASSWORD || '';

if (!ADMIN_EMAIL || !ADMIN_PASSWORD) {
  console.error('❌ Requerido: ADMIN_EMAIL y ADMIN_PASSWORD');
  console.error('   Uso: ADMIN_EMAIL=x ADMIN_PASSWORD=y node scripts/bulk-update-productos.mjs [--dry-run]');
  process.exit(1);
}

// ─── Mapeo: nombre original en BD → nombre limpio ────────────────────────────
const NAME_ALIASES = {
  'INTENZE FRUIT PUNCH 30 SER':       'INTENZE 30 SER',
  'ELECTRON AZUL 30Serv':             'ELECTRON 30Serv',
  'MEGAPLEX 2LB VAINILLA':            'MEGAPLEX 2LB',
  'PROTON GAINER 3LB VAINILLA':       'PROTON GAINER 3LB',
  'SMART GAINER 3LB VAINILLA':        'SMART GAINER 3LB',
  'TNT 3LB (VAINILLA)':               'TNT 3LB',
  'CARNIVOR 4LB, CHOCOLATE':          'CARNIVOR 4LB',
  'MANIAC PRE SANDIA':                'MANIAC PRE',
  'JOKER PRE MANGO':                  'JOKER PRE',
  'PANCAKES NUTRAMERICAN AMARILLO':   'PANCAKES NUTRAMERICAN',
  'CREATINA 150 GR IMN 50 SERV- 3GR': 'CREATINA 150 GR IMN 50 SERV 3GR',
};

// cleanName (upper) → originalName
const REVERSE_ALIASES = Object.fromEntries(
  Object.entries(NAME_ALIASES).map(([orig, clean]) => [clean.toUpperCase(), orig])
);

// ─── Datos del spreadsheet ────────────────────────────────────────────────────
const SPREADSHEET = [
  { nombre: 'BIPRO CLASSIC 2LB',                     inv: 3, costo: 136500, precio: 170000, sabores: ['Vainilla', 'Cookies and Cream'] },
  { nombre: 'BIPRO CLASSIC 3LB',                     inv: 3, costo: 185000, precio: 225000, sabores: ['Vainilla', 'Cookies and Cream'] },
  { nombre: 'WHEY PURE 2LB',                         inv: 3, costo: 112700, precio: 155000, sabores: ['Vainilla', 'Chocolate'] },
  { nombre: 'WHEY PURE 5LB',                         inv: 2, costo: 276000, precio: 350000, sabores: ['Vainilla', 'Fresa'] },
  { nombre: 'WHEY GOLD 2LB',                         inv: 2, costo: 181000, precio: 225000, sabores: ['Vainilla', 'Cookies and Cream'] },
  { nombre: 'WHEY GOLD 5LB',                         inv: 2, costo: 341000, precio: 410000, sabores: ['Vainilla', 'Chocolate'] },
  { nombre: 'ISO 100 1.3LB',                         inv: 1, costo: 164000, precio: 235000, sabores: ['Cookies and Cream'] },
  { nombre: 'ISO 100 3LB',                           inv: 0, costo: 290000, precio: 360000, sabores: ['Sin sabor'] },
  { nombre: 'ISO 100 5LB',                           inv: 2, costo: 500000, precio: 560000, sabores: ['Vainilla', 'Cookies and Cream'] },
  { nombre: 'NITROTECH 2LB',                         inv: 1, costo: 155000, precio: 200000, sabores: ['Vainilla'] },
  { nombre: 'NITROTECH 4LB',                         inv: 1, costo: 255000, precio: 320000, sabores: ['Vainilla'] },
  { nombre: 'NITROTECH 5LB',                         inv: 1, costo: 300000, precio: 350000, sabores: ['Sin sabor'] },
  { nombre: 'PLATINUM 80 SERV',                      inv: 4, costo: 130000, precio: 165000, sabores: ['Sin sabor'] },
  { nombre: 'IRON 100 SERV',                         inv: 4, costo:  80000, precio: 115000, sabores: ['Sin sabor'] },
  { nombre: 'AMINO TONE',                            inv: 1, costo: 100000, precio: 145000, sabores: ['Sin sabor'] },
  { nombre: 'AMINO ENERGY 30S',                      inv: 1, costo:  88000, precio: 128000, sabores: ['Sin sabor'] },
  { nombre: 'AMINO ENERGY 65S',                      inv: 1, costo: 169000, precio: 215000, sabores: ['Sin sabor'] },
  { nombre: 'AMINO X 30 SERV',                       inv: 2, costo:  95000, precio: 125000, sabores: ['Fruit Punch', 'Watermelon'] },
  { nombre: 'AMINO X 70 SERV',                       inv: 2, costo: 185000, precio: 220000, sabores: ['Sin sabor'] },
  { nombre: 'C4 30 SERV CELLUCOR',                   inv: 2, costo: 100000, precio: 130000, sabores: ['Sin sabor'] },
  { nombre: 'C4 50 SERV CELLUCOR',                   inv: 2, costo: 140000, precio: 175000, sabores: ['Sin sabor'] },
  { nombre: 'CAFEINA NUTRAKEY',                      inv: 1, costo:  55000, precio:  90000, sabores: ['Sin sabor'] },
  { nombre: 'CAFFEINA',                              inv: 2, costo:  40000, precio:  70000, sabores: ['Sin sabor'] },
  { nombre: 'CAFFEINA MUSCLETECH 125 TABS',          inv: 0, costo:  50000, precio:  80000, sabores: ['Sin sabor'] },
  { nombre: 'YOHIMBINA RX 100CAPS',                  inv: 0, costo:  50000, precio:  95000, sabores: ['Sin sabor'] },
  { nombre: 'CITRATO DE MAGNESIO',                   inv: 1, costo:  36000, precio:  70000, sabores: ['Sin sabor'] },
  { nombre: 'GLICINATO MAGNESIO VALEY 100CAPS',      inv: 1, costo:  48000, precio:  80000, sabores: ['Sin sabor'] },
  { nombre: 'BETA ALANINA METABOLIC',                inv: 1, costo:  84000, precio: 115000, sabores: ['Sin sabor'] },
  { nombre: 'ASHWAGANDHA HIMALAYA 60CAPS',           inv: 2, costo:  55000, precio:  75000, sabores: ['Sin sabor'] },
  { nombre: 'ASHWAGANDHA KSM-66 ALLMAX 60CAPS',      inv: 0, costo:  58000, precio:  80000, sabores: ['Sin sabor'] },
  { nombre: 'CREATINA 150 GR IMN 50 SERV 3GR',       inv: 1, costo:  34300, precio:  65000, sabores: ['Sin sabor'] },
  { nombre: 'INTENZE 30 SER',                        inv: 1, costo: 101500, precio: 135000, sabores: ['Fruit Punch'] },
  { nombre: 'ELECTRON 30Serv',                       inv: 2, costo:  87500, precio: 115000, sabores: ['Azul'] },
  { nombre: 'NITROX 200G',                           inv: 2, costo:  35000, precio:  50000, sabores: ['Sin sabor'] },
  { nombre: 'MEGAPLEX 2LB',                          inv: 2, costo:  45500, precio:  65000, sabores: ['Vainilla'] },
  { nombre: 'PROTON GAINER 3LB',                     inv: 1, costo:  59500, precio:  80000, sabores: ['Vainilla'] },
  { nombre: 'SMART GAINER 3LB',                      inv: 1, costo:  69300, precio:  90000, sabores: ['Vainilla'] },
  { nombre: 'TNT 3LB',                               inv: 1, costo:  64500, precio:  86000, sabores: ['Vainilla'] },
  { nombre: 'CARNIVOR 4LB',                          inv: 1, costo: 247000, precio: 295000, sabores: ['Chocolate'] },
  { nombre: 'BURNER STACK 60SERV',                   inv: 1, costo:  98000, precio: 135000, sabores: ['Sin sabor'] },
  { nombre: 'NITROFIT 30CAPS',                       inv: 1, costo:  85000, precio: 110000, sabores: ['Sin sabor'] },
  { nombre: 'MANIAC PRE',                            inv: 1, costo: 130000, precio: 160000, sabores: ['Sandia'] },
  { nombre: 'JOKER PRE',                             inv: 1, costo: 127000, precio: 160000, sabores: ['Mango'] },
  { nombre: 'LIPOCORE ADVANCE',                      inv: 0, costo: 107000, precio: 150000, sabores: ['Sin sabor'] },
  { nombre: 'GLUTAMINA BASIC',                       inv: 1, costo:  70000, precio:  95000, sabores: ['Sin sabor'] },
  { nombre: 'CREATINA ON 60 SERVICIOS MICRONIZADA',  inv: 1, costo: 101000, precio: 125000, sabores: ['Sin sabor'] },
  { nombre: 'CREATINA ON 120 SERVICIOS MICRONIZADA', inv: 0, costo: 144000, precio: 175000, sabores: ['Sin sabor'] },
  { nombre: 'MULTIVITAMINICO MUSCLE',                inv: 1, costo:  65000, precio: 100000, sabores: ['Sin sabor'] },
  { nombre: 'PROTON MAX 6LB',                        inv: 0, costo: 112000, precio: 140000, sabores: ['Sin sabor'] },
  { nombre: 'PASE PRE ENTRENO',                      inv: 0, costo:  73500, precio: 100000, sabores: ['Sin sabor'] },
  { nombre: 'CREATINA IMN 400G',                     inv: 0, costo:  84000, precio: 110000, sabores: ['Sin sabor'] },
  { nombre: 'TNT 10LB',                              inv: 1, costo: 200000, precio: 235000, sabores: ['Vainilla'] },
  { nombre: 'HYDROXYCUT ON',                         inv: 1, costo:  82000, precio: 112000, sabores: ['Sin sabor'] },
  { nombre: 'PANCAKES NUTRAMERICAN',                 inv: 1, costo:  30000, precio:  45000, sabores: ['Amarillo'] },
  { nombre: 'VENOM PRE ENTRENO',                     inv: 0, costo: 124000, precio: 164000, sabores: ['Sin sabor'] },
];

// Set de todos los nombres target (clean + original) para detectar productos a borrar
const TARGET_NAMES_UPPER = new Set();
for (const item of SPREADSHEET) {
  TARGET_NAMES_UPPER.add(item.nombre.toUpperCase());
  const orig = REVERSE_ALIASES[item.nombre.toUpperCase()];
  if (orig) TARGET_NAMES_UPPER.add(orig.toUpperCase());
}

// ─── Helpers ─────────────────────────────────────────────────────────────────

const norm = (s) => (s ?? '').trim().toUpperCase();

/** Distributes total stock evenly; first variant gets the remainder */
function distribuirStock(total, count) {
  const base = Math.floor(total / count);
  const resto = total - base * count;
  return Array.from({ length: count }, (_, i) => base + (i === 0 ? resto : 0));
}

async function apiJson(method, path, body, token) {
  const headers = { 'Content-Type': 'application/json' };
  if (token) headers['Authorization'] = `Bearer ${token}`;
  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  });
  const text = await res.text();
  if (!res.ok) throw new Error(`${method} ${path} → HTTP ${res.status}: ${text.slice(0, 200)}`);
  return text ? JSON.parse(text) : null;
}

async function apiFormData(path, payload, token) {
  const form = new FormData();
  form.append('producto', new Blob([JSON.stringify(payload)], { type: 'application/json' }));
  const res = await fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: { Authorization: `Bearer ${token}` },
    body: form,
  });
  const text = await res.text();
  if (!res.ok) throw new Error(`POST ${path} → HTTP ${res.status}: ${text.slice(0, 200)}`);
  return text ? JSON.parse(text) : null;
}

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

// ─── Main ─────────────────────────────────────────────────────────────────────

async function main() {
  console.log('\n🏋️  Olympo Store — Bulk Update Productos');
  console.log(`   API:  ${API_BASE}`);
  console.log(DRY_RUN ? '   Modo: DRY-RUN (sin cambios en BD)\n' : '   Modo: LIVE ⚠️\n');

  // ── Fase 1: Auth ──────────────────────────────────────────────────────────
  console.log('🔐 Autenticando...');
  const tokens = await apiJson('POST', '/auth/login', { correo: ADMIN_EMAIL, contrasena: ADMIN_PASSWORD });
  const token = tokens.accessToken;
  console.log('   ✅ Token OK\n');

  // ── Fase 2: Estado actual ─────────────────────────────────────────────────
  console.log('📡 Cargando BD...');
  const [allProductos, allSabores] = await Promise.all([
    apiJson('GET', '/productos/', null, token),
    apiJson('GET', '/sabores/', null, token),
  ]);
  console.log(`   ${allProductos.length} productos, ${allSabores.length} sabores\n`);

  // ── Fase 3: Crear sabores faltantes ───────────────────────────────────────
  const SABORES_NECESARIOS = [
    'Vainilla', 'Chocolate', 'Fresa', 'Cookies and Cream',
    'Fruit Punch', 'Watermelon', 'Sandia', 'Mango', 'Azul', 'Amarillo', 'Sin sabor',
  ];
  const saborExiste = new Set(allSabores.map((s) => norm(s.nombre)));

  console.log('🍫 Verificando sabores...');
  for (const sabor of SABORES_NECESARIOS) {
    if (!saborExiste.has(norm(sabor))) {
      console.log(`   + Crear: "${sabor}"`);
      if (!DRY_RUN) await apiJson('POST', '/sabores/', { nombre: sabor, activo: true }, token);
    }
  }
  console.log('   OK\n');

  // Index DB products by nombre (upper) for fast lookup
  const dbByNombre = new Map();
  for (const p of allProductos) {
    const key = norm(p.nombre);
    if (!dbByNombre.has(key)) dbByNombre.set(key, []);
    dbByNombre.get(key).push(p);
  }

  const stats = { updated: 0, created: 0, deleted: 0, skipped: 0, errors: 0 };

  // ── Fase 4: Sincronizar cada entrada del spreadsheet ──────────────────────
  console.log('📦 Sincronizando productos...\n');

  for (const entry of SPREADSHEET) {
    const cleanNombre = entry.nombre;
    const origNombre  = REVERSE_ALIASES[norm(cleanNombre)];

    // Collect all DB rows that match this product (by clean or original name)
    const dbRows = [
      ...(dbByNombre.get(norm(cleanNombre)) ?? []),
      ...(origNombre ? (dbByNombre.get(norm(origNombre)) ?? []) : []),
    ];

    const template  = dbRows[0] ?? null;
    const stocks    = distribuirStock(entry.inv, entry.sabores.length);
    const matchedIds = new Set();

    console.log(`📌 ${cleanNombre}  [${entry.sabores.join(' | ')}]  INV=${entry.inv}`);

    for (let i = 0; i < entry.sabores.length; i++) {
      const sabor   = entry.sabores[i];
      const cantidad = stocks[i];

      // Find matching DB row: exact sabor match first, then fallback for single-sabor products
      let existing = dbRows.find((p) => norm(p.sabor) === norm(sabor));
      if (!existing && entry.sabores.length === 1) {
        // Fallback: accept row with empty/null sabor as the variant to update
        existing = dbRows.find((p) => !p.sabor || norm(p.sabor) === '');
      }

      if (existing) {
        matchedIds.add(existing.id);
        const payload = {
          nombre:            cleanNombre,
          descripcion:       existing.descripcion || cleanNombre,
          cantidad,
          precio:            entry.precio,
          costo:             entry.costo,
          tamano:            existing.tamano ?? '',
          sabor,
          categoria:         existing.categoria,
          marca:             existing.marca,
          imagenGeneral:     existing.imagenGeneral     ?? null,
          imagenNutricional: existing.imagenNutricional ?? null,
        };
        console.log(`   ✏️  UPDATE  id=${existing.id}  sabor="${sabor}"  qty=${cantidad}  precio=$${entry.precio.toLocaleString()}`);
        if (!DRY_RUN) {
          try {
            await apiJson('PUT', `/productos/id/${existing.id}`, payload, token);
            stats.updated++;
          } catch (e) {
            console.error(`   ❌ ${e.message}`);
            stats.errors++;
          }
          await sleep(80);
        } else {
          stats.updated++;
        }
      } else if (template) {
        const payload = {
          nombre:      cleanNombre,
          descripcion: template.descripcion || cleanNombre,
          cantidad,
          precio:      entry.precio,
          costo:       entry.costo,
          tamano:      template.tamano ?? '',
          sabor,
          categoria:   template.categoria,
          marca:       template.marca,
        };
        console.log(`   ➕ CREATE  sabor="${sabor}"  qty=${cantidad}  precio=$${entry.precio.toLocaleString()}`);
        if (!DRY_RUN) {
          try {
            await apiFormData('/productos/', payload, token);
            stats.created++;
          } catch (e) {
            console.error(`   ❌ ${e.message}`);
            stats.errors++;
          }
          await sleep(80);
        } else {
          stats.created++;
        }
      } else {
        console.log(`   ⚠️  SKIP CREATE  sabor="${sabor}" — no hay template (producto no existe en BD)`);
        stats.skipped++;
      }
    }

    // Delete extra variants (same product name, sabor not in target list)
    const targetSaboresUp = new Set(entry.sabores.map(norm));
    const extraRows = dbRows.filter(
      (p) => !matchedIds.has(p.id) && !targetSaboresUp.has(norm(p.sabor))
    );
    for (const extra of extraRows) {
      console.log(`   🗑️  DELETE  id=${extra.id}  sabor="${extra.sabor}" (variante extra)`);
      if (!DRY_RUN) {
        try {
          await apiJson('DELETE', `/productos/id/${extra.id}`, null, token);
          stats.deleted++;
        } catch (e) {
          console.error(`   ❌ ${e.message}`);
          stats.errors++;
        }
        await sleep(80);
      } else {
        stats.deleted++;
      }
    }
  }

  // ── Fase 5: Eliminar productos no listados ────────────────────────────────
  console.log('\n🗑️  Buscando productos no listados...');
  const toDelete = allProductos.filter((p) => !TARGET_NAMES_UPPER.has(norm(p.nombre)));

  if (toDelete.length === 0) {
    console.log('   Ninguno — todos los productos de BD están en el spreadsheet.');
  } else {
    console.log(`   ${toDelete.length} producto(s) a eliminar:`);
    for (const p of toDelete) {
      console.log(`   🗑️  DELETE  id=${p.id}  nombre="${p.nombre}"  sabor="${p.sabor}"`);
      if (!DRY_RUN) {
        try {
          await apiJson('DELETE', `/productos/id/${p.id}`, null, token);
          stats.deleted++;
        } catch (e) {
          console.error(`   ❌ ${e.message}`);
          stats.errors++;
        }
        await sleep(80);
      } else {
        stats.deleted++;
      }
    }
  }

  // ── Resumen ───────────────────────────────────────────────────────────────
  const label = DRY_RUN ? '(planificadas)' : '(ejecutadas)';
  console.log('\n═══════════════════════════════════════════');
  console.log(`✅ Operaciones ${label}:`);
  console.log(`   Actualizados : ${stats.updated}`);
  console.log(`   Creados      : ${stats.created}`);
  console.log(`   Eliminados   : ${stats.deleted}`);
  console.log(`   Saltados     : ${stats.skipped}`);
  console.log(`   Errores      : ${stats.errors}`);
  if (DRY_RUN) {
    console.log('\n   ⚠️  Ejecutar sin --dry-run para aplicar los cambios.');
  }
  console.log('═══════════════════════════════════════════\n');
}

main().catch((e) => {
  console.error('\n💥 Error fatal:', e.message);
  process.exit(1);
});
