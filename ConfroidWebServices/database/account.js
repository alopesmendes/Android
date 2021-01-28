const knex = require('./db');


function getAllAccount() {
    return knex("account").select("*");
}

function findByUsername(username) {
    return knex("account").where({ username: username});
}

async function findByUsernamePassword(username, password) {
    var results = await knex('account').where({      // Query: SELECT * FROM users
        'username': username,                   //                 WHERE login = ?
        'password': password,                   //                 AND password = ?
      }).first().then(function(user) {
        if(user){
            console.log('pp' + JSON.stringify(user));
            return JSON.stringify(user);        
        } else {
            return null;
        }
    }).catch( (err) => JSON.stringify('Wrong credentials'));

    return results;
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