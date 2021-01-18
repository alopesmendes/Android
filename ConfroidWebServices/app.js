const express = require('express');
const path = require("path");
var fs = require('fs');
const app = express();
const bodyParser = require("body-parser");


// app config
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(express.static('public'));


// Defining each routes with its file
const loginRoutes = require('./routes/login');
const dashboardRoutes = require('./routes/dashboard');


// Routes that should handle requests
app.use('/', loginRoutes);
app.use("/dashboard", dashboardRoutes);






const courses = [
    { id:1, name:'english', teachers:['aAAA', 'Bbbb', 'CCcC']},
    { id:2, name:'course2', teachers:[] },
    { id:3, name:'course3', teachers:['aAAA'] },
];

const school = {
    classroom: "AB2",
    classes:courses,
}



app.get('/api/courses', (req, res) => {
    res.json(school);
});

app.post('/api/courses', (req, res) => {
    const newCourse = req.body;
    school.classes.push(newCourse);
    res.json(school);
});


// Offer this app to other files that may require it
module.exports = app;