/**
 * Created by hackbansu on 9/4/17.
 */
const database = require('../database/database.js');
const router = require('express').Router;
const route = router();
const multer = require('multer');
const request = require('request');
const fs = require('fs');

//multer config
var upload = multer({dest: 'uploads/'})

let notification = [];


//body = {name, goingOut}
route.post('/vehiclePicUpload', upload.single('vehicle'), function (req, res, next) {
    // req.file is the `vehicle` file
    // req.body will hold the text fields, if there were any

    //do image processing
    let vehicleType = "four"
    /*Each file contains the following information:

            Key	Description	Note
    fieldname	:   Field name specified in the form
    originalname	:   Name of the file on the user's computer
    encoding	:   Encoding type of the file
    mimetype	:   Mime type of the file
    size	:   Size of the file in bytes
    destination	:   The folder to which the file has been saved	DiskStorage
    filename	:   The name of the file within the destination	DiskStorage
    path	:   The full path to the uploaded file	DiskStorage
    buffer	:   A Buffer of the entire file	MemoryStorage
    */

    //update database
    let goingOut = req.body.goingOut;
    database.parkingStatusTable.updateAvail({name: req.body.name}, {
        goingOut,
        vehicleType
    }, function (err, result, fields) {
        if (err) {
            console.log(err);
            return res.json({status: false, message: 'some error occured while updating database'});
        }
        return res.json({status: true, message: "availability updated"});
    })
});

//body = {goingOut, name}
route.post('/numPicUpload', upload.single('num'), function (req, res, next) {
    // req.file is the `num` file
    // req.body will hold the text fields, if there were any

    console.log("inside numPic");
    console.log(req.body);
    // if(!req.file){
    //     console.log(req.body);
    //     return res.json({status: false, message: "no file found"});
    // }

    //do image processing
    var formData = {
        image: fs.createReadStream(__dirname + '/../uploads/' + req.body.image),
    };
    request.post({
        url: 'https://api.openalpr.com/v2/recognize?recognize_vehicle=1&country=india&secret_key=sk_2c4ad5b4dd5e83192efc5e9d',
        formData: formData
    }, function optionalCallback(err, httpResponse, body) {
        if (err) {
            console.error('upload failed:', err);
            return res.json({status: false, message: 'upload failed'});
        }
        body = JSON.parse(body);
        console.log('Upload successful!  Server responded with:', body['results'][0].plate);

        let vehNum = body.results[0].plate;

        //update database
        let name = req.body.name, goingOut = parseInt(req.body.goingOut);
        if (goingOut === 0) {     //vehicle going inside parking
            database.vehiclesTable.insert({name: name, veh_num: vehNum}, function (err, result, fields) {
                if (err) {
                    console.error(err);
                    return res.json({status: false, message: 'error in database'});
                }

                //notify the app for e-receipt
                notification.push({message: "parking receipt"});

                return res.json({status: true, message: "success"})
            })
        } else {
            database.vehiclesTable.get({name: name, veh_num: vehNum}, function (err, result, fields) {
                if (err || (result.length === 0)) {
                    console.error(err);
                    return res.json({status: false, message: 'error in database'});
                }

                // calculate charges
                // let stayTime = (new Date() - result[0].entry_time);
                let charge = 40;

                //notify app for payment
                notification.push({message: "process payment"});

                //remove from db
                database.vehiclesTable.remove({name: name, veh_num: vehNum}, function (err, result, fields) {
                    if (err) {
                        console.error(err);
                        return res.json({status: false, message: 'error in database'});
                    }

                    return res.json({status: true, message: "paid via ..."})
                })
            })
        }

    });
});

route.get('/notifications', function (req, res) {
    if(notification.length === 0) {
        return res.json({status: false, message: notification});
    }
    return res.json({status: true, message: notification});
});

module.exports = route;