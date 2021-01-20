var express = require('express');
var dataModel = require('../models/data-model.js');
var router = express.Router();
const path = require('path');

/* GET login page. */
router.get('/', function(req, res, next) {
  res.sendFile(path.join(__dirname,'../views/login.html'));
});


router.post('/', (req, res) => {


  const username = req.body.username;
  const password = req.body.password;

  //console.log("Username: " + username);
  //console.log("password: " + password);
  res.json(req.body);
  //console.log("password: " + password);

});



module.exports = router;
