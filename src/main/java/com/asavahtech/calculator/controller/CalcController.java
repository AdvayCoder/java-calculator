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
    String tempNum = "";  //store the number here before a non-num btn is clicked`
    
    double currentOutput = 0.0;
    
    public CalcController() {
        
    }
    
    public String addToExpression(String fragment) {
        if(fragment.equals("+") || fragment.equals("-") || fragment.equals("*") || fragment.equals("/") || fragment.equals("%")) {
            if(!tempNum.equals("")) {
                calcExpression.add(tempNum);
            }
            
            String prevTerm = "";
            
            //get previous term
            if(calcExpression.size() - 1 > -1) {
                prevTerm = calcExpression.get(calcExpression.size() - 1);
            }
            
            if(prevTerm.equals("+") || prevTerm.equals("-") || prevTerm.equals("*") || prevTerm.equals("/")) {
                return "";
            }
            
            calcExpression.add(fragment);
            System.out.println(calcExpression);
            tempNum = ""; //clear temp num
            
            if(fragment.equals("*")) {
                return "×";
            }
            if(fragment.equals("/")) {
                return "÷";
            }
            
            return fragment; //stop method from proceding further
        }        
        
        tempNum += fragment;
        return fragment;
    }
    
    public void clearExpression() {
        calcExpression.clear();
        tempNum = "";
    }
    
    public void deleteLastCharacter() {
        //delete last character of number currently being typed
        if(!tempNum.equals("")) {
            tempNum = tempNum.substring(0, tempNum.length() - 1);
            return;
        }
        
        //if there are no characters to delete
        if(calcExpression.size() - 1 == -1) {
            return;
        }
        
        //previous term
        int index = calcExpression.size() - 1;
        
        String previousElement = calcExpression.get(index);
        
        //to remove the element if further removal is not possible
        if(previousElement.length() == 1) {
            calcExpression.remove(index);
            return;
        }
        
        //remove last character of string
        calcExpression.set(index, previousElement.replaceAll(".$", ""));
        
        System.out.println(calcExpression);
    }
    
    public double computeExpression() {
        if(!tempNum.equals("")) {
            calcExpression.add(tempNum);
        }
        
        double finalOutput = parseExpression();
        
        //task 1
        //add code to round output returned as double to 7 digits
        
        //task 2
        //if the output modulo 2 is 1 or 0 then remove the ".0" at the end of the output
        //eg. In 5.0, the ".0" should be removed
        //The opearator for modulo is "%"
        //eg. 5 % 2 = 1 (because the remainder of the division of 5 and 2 is 1)
        
        clearExpression();
        
        currentOutput = finalOutput;
        
        calcExpression.add(Double.toString(finalOutput));
        
        return finalOutput;
    }
    
    public double parseExpression() {
        ArrayList<String> firstParse = new ArrayList<String>();
        ArrayList<String> secondParse = new ArrayList<String>();
        
        double finalOutput = 0;
        
        //first for multiplying
        for (int i = 0; i < calcExpression.size(); i++) {
            if(calcExpression.get(i).equals("*")) {
                double prevNum = Double.parseDouble(calcExpression.get(i - 1));
                double nextNum = Double.parseDouble(calcExpression.get(i + 1));
                
                String sign = (i - 2) > -1 ? calcExpression.get(i - 2) : "";
                
                double multipliedTerm = prevNum * nextNum;
                
                if(sign.equals("-")) {
                    multipliedTerm = -(multipliedTerm);
                }
                
                System.out.println(multipliedTerm);
                
                firstParse.remove(firstParse.size() - 1); //remove element before multiplication symbol
                
                //to remove the 'add' or 'subtract' symbols, so that it is easier to calculate in a later step
                if((firstParse.size() - 1) > -1 && (firstParse.get(firstParse.size() - 1).equals("+") || firstParse.get(firstParse.size() - 1).equals("-"))) {
                    firstParse.remove(firstParse.size() - 1);
                }
                
                firstParse.add(Double.toString(multipliedTerm));
                continue;
            }
            
            if((i-1) > -1 && calcExpression.get(i - 1).equals("*")) {
                continue;
            }
            
            firstParse.add(calcExpression.get(i)); 
        }
        
        //,then for dividing
        for (int i = 0; i < firstParse.size(); i++) {
            if(firstParse.get(i).equals("/")) {
                double prevNum = Double.parseDouble(firstParse.get(i - 1));
                double nextNum = Double.parseDouble(firstParse.get(i + 1));
                
                String sign = i > 2 ? calcExpression.get(i - 2) : "";
                
                double dividedTerm = prevNum / nextNum;
                
                if(sign.equals("-")) {
                    dividedTerm = -(dividedTerm);
                }
                
                secondParse.remove(secondParse.size() - 1); //remove element before multiplication symbol
                
                //to remove the 'add' or 'subtract' symbols, so that it is easier to calculate in a later step
                if((secondParse.size() - 1) > -1 && (secondParse.get(secondParse.size() - 1).equals("+") || secondParse.get(secondParse.size() - 1).equals("-"))) {
                    secondParse.remove(secondParse.size() - 1);
                }
                
                secondParse.add(Double.toString(dividedTerm));
                continue;
            }
            
            if((i-1) > -1 && firstParse.get(i - 1).equals("/")) {
                continue;
            }
            
            secondParse.add(firstParse.get(i));
        }
        
        for (int i = 0; i < secondParse.size(); i++) {
            if(secondParse.get(i).equals("+")) {
                secondParse.remove(i);
            }
            
            if(secondParse.get(i).equals("-")) {
                double negatedNum = -(Double.parseDouble(secondParse.get(i + 1)));
                
                secondParse.set((i + 1), Double.toString(negatedNum));
                
                secondParse.remove(i);
            }
        }
        
        //for the final output
        for(String num : secondParse) {
            System.out.println(num);
            finalOutput += (Double.parseDouble(num));
        }  
        
        return finalOutput;
    }
    
    
    public double getCurrentOutput() {
        return currentOutput;
    }
}

