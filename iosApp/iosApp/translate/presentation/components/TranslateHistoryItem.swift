//
//  TranslateHistoryItem.swift
//  iosApp
//
//  Created by Barış Semerci on 24.03.2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
 import shared
 
 struct TranslateHistoryItem: View {
     let item: UiHistoryItem
     let onClick: () -> Void
     
     var body: some View {
         Button(action: onClick) {
             VStack(alignment: .leading) {
                 HStack {
                     SmallLanguageIcon(language: item.fromLanguage)
                         .padding(.trailing)
                     Text(item.fromText)
                         .foregroundColor(.lightBlue)
                         .font(.body)
                 }
                 .frame(maxWidth: .infinity, alignment: .leading)
                 .padding(.bottom)
                 
                 HStack {
                     SmallLanguageIcon(language: item.toLanguage)
                         .padding(.trailing)
                     Text(item.toText)
                         .foregroundColor(.onSurface)
                         .font(.body.weight(.semibold))
                 }
                 .frame(maxWidth: .infinity, alignment: .leading)
             }
             .frame(maxWidth: .infinity)
             .padding()
             .gradientSurface()
             .cornerRadius(15)
             .shadow(radius: 4)
         }
     }
 }
 
 struct TranslateHistoryItem_Previews: PreviewProvider {
     static var previews: some View {
         TranslateHistoryItem(
             item: UiHistoryItem(
                 id: 0,
                 fromLanguage: UiLanguage(language: .english, imageName: "english"), fromText: "Hello",
                 toLanguage: UiLanguage(language: .german, imageName: "german"), toText: "Hallo"
             ),
             onClick: {}
         )
     }
 }
