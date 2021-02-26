const knex = require("knex");
const sqlite3 = require('sqlite3').verbose();

// open database
var db = new sqlite3.Database('./database/db.sqlite', (err) => {
  if (err) {
    return console.error(err.message);
  }
  console.log('Connected to the in-memory SQlite database.');
});


db.serialize(() => {
  // Queries scheduled here will be serialized.
  db.run('DROP TABLE account');
  db.run('CREATE TABLE IF NOT EXISTS account(id_person INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE, password TEXT NOT NULL)')
    .run(`INSERT INTO account(username, password)
          VALUES('a', 'a'),
                ('b', 'b'),
                ('Z', 'Z')`)
    .each(`SELECT * FROM account`, (err, row) => {
      if (err){
        throw err;
      }
      console.log(row);
    });
});


// open database in memory
//let db = new sqlite3.Database(':memory:');




const connectedKnex = knex({
  client: "sqlite3",
  connection: {
      filename: "database/db.sqlite"
  },
  useNullAsDefault: true
})


module.exports = connectedKnex
//module.exports = db;
