//
//  WeatherViewController.swift
//  WeatherApp
//
//  Created by Oliver Gepp on 17.01.17.
//  Copyright © 2017 Zuehlke Engineering AG. All rights reserved.
//

import UIKit
import CoreLocation

/*
 * Brugg AG: 47.479501, 8.213011
 */

class WeatherViewController: UIViewController {
    let locationManager = CLLocationManager()
    
    fileprivate var weatherRequest: WeatherRequest? = nil
    fileprivate var location: CLLocation? = nil
    
    @IBOutlet weak var cityLabel: UILabel!
    @IBOutlet weak var weatherLabel: UILabel!
    @IBOutlet weak var tempLabel: UILabel!
    @IBOutlet weak var minTempLabel: UILabel!
    @IBOutlet weak var maxTempLabel: UILabel!
    @IBOutlet weak var humidityLabel: UILabel!
    @IBOutlet weak var iconImageView: UIImageView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        locationManager.delegate = self
        locationManager.requestLocation()
        askForPermission()
    }
    
    
    fileprivate func showDeviceNotAllowed() {
        
        let alertController = UIAlertController(title: "Entschuldigung", message: "Die Standortbestimmung ist für die App nicht freigegeben", preferredStyle: .alert)
        let cancelAction = UIAlertAction(title:"OK", style: .cancel, handler: nil)
        alertController.addAction(cancelAction)
        self.present(alertController, animated: true, completion: nil)
    }
    
    
    fileprivate func performServerRequest() {
        
        guard let location = self.location else {
            print("No location detemined")
            return
        }
        
        let request = WeatherRequest(latitude: location.coordinate.latitude, longitude: location.coordinate.longitude)
        request.performRequest(successHandler: { (weatherData: WeatherData) in
            
            DispatchQueue.main.async {
                self.updateUI(weatherData: weatherData)
            }

        }) { 
            print("error loading weather")
        }
    }
    
    private func updateUI(weatherData:WeatherData){
    
        cityLabel.text = weatherData.city
        weatherLabel.text = weatherData.weather
        tempLabel.text = weatherData.formattedTemp
        minTempLabel.text = weatherData.formattedMinTemp
        maxTempLabel.text = weatherData.formattedMaxTemp
        humidityLabel.text = weatherData.formattedHumidity
        
        if let imageName = weatherData.icon{
            let imageRequest = WeatherIconRequest(iconName: imageName)
            imageRequest.performRequest(successHandler: { (image : UIImage) in
                DispatchQueue.main.async {
                    self.updateImage(image: image)
                }
            }, errorHandler: { 
                print("error loading weather icon")
            })
        }
    }
    
    private func updateImage(image: UIImage){
        iconImageView.image = image
    }
    
    
    @IBAction func reloadPressed(_ sender: AnyObject) {
        locationManager.requestLocation()
    }
}


extension WeatherViewController : CLLocationManagerDelegate{
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        guard  let location = locations.first else {
            return
        }
        
        print("Received location \(location)")
        
        self.location = location
        locationManager.stopUpdatingLocation()
        
        performServerRequest()
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Requesting location failed: \(location)")
    }
    
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        
        print("Did change authorization")
        if status == CLAuthorizationStatus.authorizedWhenInUse {
            startLocationRequest()
        }
    }
    

    fileprivate func checkPermission() {
        
        print("Checking permission")
        switch(CLLocationManager.authorizationStatus()) {
        case .denied:
            showDeviceNotAllowed()
        case .restricted:
            showDeviceNotAllowed()
        case .notDetermined:
            askForPermission()
        case .authorizedWhenInUse:
            startLocationRequest()
        default:
            print("other status, do nothing")
        }
    }
    
    
    fileprivate func askForPermission() {
        print("Requesting permission")
        locationManager.requestWhenInUseAuthorization()
    }
    
    fileprivate func startLocationRequest() {
        print("Start location request")
        locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters
        locationManager.startUpdatingLocation()
    }
    
}

