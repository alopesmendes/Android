
var AuthHelper = require('./utils_auth');

var express = require('express');
var router = express.Router();
const path = require('path');

const dbAccount = require("../../database/account");
const jwt = require("jsonwebtoken");



router.post('/login', async function(req, res) {
    var usernameGiven = req.body.username;                  // Get data from request body
    var passwordGiven = req.body.password;
    
    if (!usernameGiven || !passwordGiven) {
        res.json({success: false, data: 'Please enter email and password.'});
        return;
    } 

    try {
      var userInfo = await dbAccount.findByUsernamePassword(usernameGiven, passwordGiven);

      if (userInfo.length == 1) {              // Check if credentials matched
    
        const token = AuthHelper.generateToken(userInfo);
        res.status(200).json({token});
        //res.send(`Hello, ${users[0].username}`);
        console.log(token);
        //res.header('Authorization', "Bearer "+ token).redirect('/api/auth/');
        //res.header('x-authorization', "Bearer "+ token).redirect('/api/auth/');
    } else {
        res.status(401).send({data : 'Wrong username or password'});
      }
    } catch (err) {                              // Handle errors
      console.error('Database error:', err);  
      res.status(500).send('Error');
    }
});

router.post("/register", async (req, res) => {
      //const hashedPassword = await bcrypt.hash(req.body.password, 10);
  
      var username = req.body.username;                  // Get data from request body
      var password = req.body.password;
      
      if (!username || !password) {
        res.json({success: false, data: 'Please enter email and password.'});
        return;
      } 

      try {
        var users = await dbAccount.findByUsername(username);
    
        if (users.length == 1) {                      // Check if credentials matched
            res.status(401).json({"data": "Username already taken" });
        } else {
            //const hashPassword = AuthHelper.hashPassword(req.body.password);

            const data = await dbAccount.insert(username, password);
            console.log(data);
            res.status(200).json({data : `User '${username}' inserted  succesfully !`}); 
        }
      } catch (err) {                              // Handle errors
        console.error('Database error:', err);     
        res.status(500).send('Error');
        //res.render("/login");
    }

    
});




router.get('/', AuthHelper.checkToken, async (req, res) => {
    const accounts = await dbAccount.getAllAccount();
    res.status(201).json({id : accounts});
});



module.exports = router;