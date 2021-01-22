var express = require('express');
var dataModel = require('../models/data-model.js');
var router = express.Router();
const path = require('path');
const db = require('../database/db');

/* GET login page. */
router.get('/', function(req, res, next) {
  res.sendFile(path.join(__dirname,'../views/login.html'));
});


function existsUser(a, b, db, callback) {

    var query = 'SELECT username, password FROM account WHERE username = ? AND password = ?'
    db.get(query, [a, b], (err, row) => {
        if (err) {
            callback(false)
        } else {
            if (row == undefined) {
                callback(false);
            } else {
                callback(true);
            }
        }
    });
}


router.post('/', (req, res) => {

  const username = req.body.username;
  const password = req.body.password;

  existsUser(username, password, db, function(condition) {
    if (condition) {
        res.status(200).json(req.body);
    } else {
        res.status(404).json({'username':'Not found', 'password':'Not found'});
    }
  });
  
  //console.log("Username: " + username);
  //console.log("password: " + password);
  //res.json(req.body);
  //console.log("password: " + password);

});



module.exports = router;
