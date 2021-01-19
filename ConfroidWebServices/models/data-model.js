var db = require('../database/db');



function createRow (table, cb) {
  let sql = `INSERT INTO ${table} DEFAULT VALUES`;
  db.run(sql, cb);
};



module.exports = {   
	createRow
}