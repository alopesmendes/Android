/*
var http = require('http');

var server = http.createServer();

server.on('connection', (socket) => {
    console.log('New connection...');
});

server.listen(3000);

console.log('Listening on port 3000');
*/

const express = require('express');
const app = express();

app.get('/', (req, res) => {
    res.send('Hello World');
});

app.listen(8080, () => {
    console.log('Server up and run');
});