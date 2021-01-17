const express = require('express');
const bodyParser = require("body-parser");
const router = express.Router();
const app = express();

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.use("/", router);

const PORT = process.env.PORT || 8080;

app.listen(PORT, () => {
    console.log(`Votre app est disponible sur localhost: ${PORT}`);
});