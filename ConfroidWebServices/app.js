const express = require('express');
const path = require("path");
var fs = require('fs');
const app = express();
const bodyParser = require("body-parser");
const jwt = require("jsonwebtoken");

global.__basedir = __dirname;
global.baseUrl = "http://localhost:8080/";

// app config
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(express.static('public'));


// Defining each routes with its file
const loginRoutes = require('./routes/auth');
const registerRoutes = require('./routes/auth');
const dashboardRoutes = require('./routes/dashboard');

const apiAuthRoutes = require('./routes/api/api_auth');
const apiFilesRoutes = require('./routes/api/api_file');


// Routes that should handle requests
app.use('/', loginRoutes);
app.use("/register", registerRoutes);
app.use("/dashboard", dashboardRoutes);

app.use("/api/auth", apiAuthRoutes);
app.use("/api/", apiFilesRoutes);



// Offer this app to other files that may require it
module.exports = app;