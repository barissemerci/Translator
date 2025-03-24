//
//  TextToSpeech.swift
//  iosApp
//
//  Created by Barış Semerci on 24.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//


import Foundation
import AVFoundation

struct TextToSpeech {
    
    private let synthesizer = AVSpeechSynthesizer()
    
    func speak(text: String, language: String) {
        let utterance = AVSpeechUtterance(string: text)
        utterance.voice = AVSpeechSynthesisVoice(language: language)
        utterance.volume = 1
        synthesizer.speak(utterance)
    }
}
