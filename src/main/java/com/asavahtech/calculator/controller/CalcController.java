/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.asavahtech.calculator.controller;
import java.util.ArrayList;

/**
 *
 * @author palni
 */
public class CalcController {
    //we will have to take into account the PEMDAS rules
    
    //change this to ArrayList
    ArrayList<String> calcExpression = new ArrayList<String>();
    
    public CalcController() {
        
    }
    
    public void addToExpression(String fragment) {
        calcExpression.add(fragment);
        System.out.println(calcExpression);
        
        String tempNum = "";  //store the number here before a non0num btn is clicked
    }
    
    public double addNumsFromArrasy() {
        return 0.0;  //for now to avoid error of no return
    }
}