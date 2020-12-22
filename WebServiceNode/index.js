const express = require('express');
const bodyParser = require("body-parser");
const router = express.Router();
const app = express();

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

const courses = [
    { id:1, name:'english', teachers:['aAAA', 'Bbbb', 'CCcC']},
    { id:2, name:'course2', teachers:[] },
    { id:3, name:'course3', teachers:['aAAA'] },
];

const school = {
    classroom: "AB2",
    classes:courses,
}

app.get('/', (req, res) => {
    res.send('Hello World ON est la!!!');
});

app.get('/api/courses', (req, res) => {
    res.json(school);
});

app.post('/api/courses', (req, res) => {
    const newCourse = req.body;
    school.classes.push(newCourse);
    res.json(school);
});

app.use("/", router);

const PORT = process.env.PORT || 8080;

app.listen(PORT, () => {
    console.log(`Votre app est disponible sur localhost: ${PORT}`);
});