const knex = require('./db');


function getAllAccount() {
    return knex("account").select("*");
}

function findByUsername(username) {
    return knex("account").where({ username: username});
}

function findByUsernamePassword(username, password) {
    return knex('account').where({      // Query: SELECT * FROM users
        'username': username,           //                 WHERE login = ?
        'password': password,           //                 AND password = ?
      });
}


function insert(username, password) {
    return knex("account").insert({ 
        username: username, 
        password: password
    });
}

module.exports = {
    insert,
    findByUsername,
    findByUsernamePassword,
    getAllAccount
};