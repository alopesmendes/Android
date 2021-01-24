var express = require('express');
var dataModel = require('../models/data-model.js');
var router = express.Router();
const path = require('path');
const db = require('../database/db');



/* GET login page. */
router.get('/', function(req, res, next) {
  res.sendFile(path.join(__dirname,'../views/login.html'));
});


/* GET register page. */
router.get('/register', function(req, res, next) {
    res.sendFile(path.join(__dirname,'../views/register.html'));
});



function existsUserPassword(username, password, db, callback) {
    var query = 'SELECT username, password FROM account WHERE username = ? AND password = ?'
    db.get(query, [username, password], (err, results) => {
        if (err) {
            callback(false)
        } else {
            if (results == undefined) {
                callback(false);
            } else {
                callback(true);
            }
        }
    });
}


function existsUser(username, callback) {

    var query = 'SELECT username FROM account WHERE username = ?'
    db.get(query, [username], (err, results) => {
        if (err) {
            callback(false)
        } else {
            if (results == undefined) {
                callback(false);
            } else {
                callback(true);
            }
        }
    });
}

/* POST in the login page. */
router.post('/', (req, res) => {

  const username = req.body.username;
  const password = req.body.password;

  existsUserPassword(username, password, db, function(condition) {
    if (condition) {
        res.status(200).json(req.body);
    } else {
        res.status(404).json({'username':'Not found', 'password':'Not found'});
    }
  });
});

/* POST in the register page. */
router.post('/register', (req, res) => {

    const username = req.body.username;
    const password = req.body.password;
  
    var appData = {};
  
    var query = 'SELECT username FROM account WHERE username = ?'

    var row = db.get(query, [username], (err, results) => {
    if (err) {
      appData.error = 1;
      appData["data"] = "Error Occured!";
      res.status(400).json(appData);
    } else {
        if (results == undefined) {
            db.run('INSERT INTO account(username, password) VALUES (?, ?)', [username, password], function(err) {
                if (err) {
                  return console.log(err.message);
                }
            });
            console.log(req.body);
            res.status(200).json(req.body);  
        } else {
            //console.log('data taken');
            res.status(400).json({"data": "Already taken" });
        }
        return row;
     }
    })

  });

module.exports = router;
