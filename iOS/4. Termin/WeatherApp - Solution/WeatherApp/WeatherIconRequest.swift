//
//  WeatherIconRequest.swift
//  WeatherApp
//
//  Created by Oliver Gepp on 17.01.17.
//  Copyright © 2017 Zuehlke Engineering AG. All rights reserved.
//

import Foundation
import UIKit


class WeatherIconRequest {

    
    private var iconName: String?
    
    init(iconName: String) {
        self.iconName = iconName
    }


    func performRequest(successHandler: @escaping (UIImage) -> Void, errorHandler: @escaping () -> Void) {
        
        guard let iconName = iconName else{
            print("no iconName set")
            errorHandler()
            return
        }
        
        
            let urlStr = "http://openweathermap.org/img/w/\(iconName).png"
            guard let url = URL(string: urlStr) else {
                print("invalid url")
                return
            }
            
            let task = URLSession.shared.dataTask(with: url) { (data:Data?, response: URLResponse?, error: Error?) in
                
                if let error = error{
                    print(error)
                    return
                }
                
                if let response = response as? HTTPURLResponse{
                    
                    if response.statusCode >= 400{
                        print("Computer says NO: \(response.statusCode)")
                        return
                    }
                }
                else {
                    print("no response received")
                    return
                }
                
                guard let data = data else {
                    print("No data received")
                    return
                }
                
                if let image =  UIImage(data: data){
                    successHandler(image)
                }
                else{
                    print("error loading image")
                    errorHandler()
                }
            }
            
            task.resume()
        }
}
