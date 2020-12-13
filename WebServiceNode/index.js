const express = require('express');
const app = express();

const handlebars = require('express-handlebars');


const courses = [
    { id:1, name:'course1' },
    { id:2, name:'course2' },
    { id:3, name:'course3' },
];

app.set('view engine', 'handlebars');

app.engine('handlebars', handlebars({
    layoutsDir: __dirname + '/views/layouts',
}));

app.get('/', (req, res) => {
    //res.render('main', {layout : 'index'});
    res.send('Hello World!!!');
});

app.get('/api/courses', (req, res) => {
    res.send(courses);
});

app.use(express.static('public'));

app.listen(8080, () => {
    console.log('Votre app est disponible sur localhost:8080 !');
});