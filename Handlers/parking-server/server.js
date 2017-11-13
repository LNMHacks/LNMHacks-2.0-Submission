/**
 * Created by pawan on 7/3/17.
 */
const express = require('express');
const bodyParser = require('body-parser');
const multer = require('multer');
const cookieParser = require('cookie-parser');
const path = require('path');
const database = require('./database/database.js')

const app = express();
app.use(cookieParser());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

let routes = {
    statusPay: require('./routes/statusPay')
};

//Handling users related requests
app.use('/statusPay', routes.statusPay);
app.post('/getParkings', function (req, res) {
    console.log(req.body);
   database.parkingStatusTable.get({}, function (err, result, fields) {
       if (err) {
           console.error(err);
           return res.json({status: false, message: 'error in database'});
       }

       return res.json({status: true, message: result});
   })
});


app.get('/', function (req, res) {
    res.send('working');
})

app.listen(4000, function () {
    console.log("server started at 4000");
});
