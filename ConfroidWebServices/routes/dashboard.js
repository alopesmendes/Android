var express = require('express');
const path = require('path');
var router = express.Router();

/* GET dashboard page. */
router.get('/', function(req, res, next) {
  res.sendFile(path.join(__dirname,'../views/dashboard.html'));
});

module.exports = router;
