const sqlite3 = require('sqlite3').verbose();
//const nameDB = 'main.db'
//const db2 = new sqlite3.Database(nameDB);

// open database
var db = new sqlite3.Database('./database/db.sqlite', (err) => {
  if (err) {
    return console.error(err.message);
  }
  console.log('Connected to the in-memory SQlite database.');
});


db.serialize(() => {
  // Queries scheduled here will be serialized.
  db.run('CREATE TABLE IF NOT EXISTS account(username TEXT, password TEXT)')
    .run(`INSERT INTO account(username, password)
          VALUES('a', 'a'),
                ('b', 'b'),
                ('c', 'c')`)
    .each(`SELECT username, password FROM account`, (err, row) => {
      if (err){
        throw err;
      }
      console.log(row.username);
    });
});


// open database in memory
//let db = new sqlite3.Database(':memory:');


module.exports = db;