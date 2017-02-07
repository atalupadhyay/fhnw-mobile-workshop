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
    
    
    //TODO: Mit Storyboard verknüpfen
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
        
        //TODO WeatherRequest ausführen
        //Mit Ergebnis updateUI aufrufen, aber im Main-Thread DispatchQueue.main.async {}
        
    }
    
    private func updateUI(weatherData:WeatherData){
    
        // UI aktualisieren
        
        //Bonus: Weather-Icon analog WeatherRequest laden: http://openweathermap.org/img/w/10d.png
        
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

