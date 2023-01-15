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
    double memNum = 0.0;
    
    int backParenthasesNeededCount; //the number of back parentheses needed to match the current number of front parentheses

    public CalcController() {

    }

    public String addToExpression(String fragment) {
        
        if(fragment.equals("(")) {
            //adds 1 to the total count of back parentheses needed
            backParenthasesNeededCount++;
        }
        
        if(fragment.equals(")")) {
            //one less back parenthesis is now needed
            backParenthasesNeededCount--;
        }

        if (fragment.equals("+") || fragment.equals("-") || fragment.equals("*") || fragment.equals("/") || fragment.equals("(") || fragment.equals(")")) {

            if (!tempNum.equals("")) {
                calcExpression.add(tempNum);
            }

            String prevTerm = getPrevTerm();

            if (!fragment.equals("(") && !fragment.equals(")") && (prevTerm.equals("+") || prevTerm.equals("-") || prevTerm.equals("*") || prevTerm.equals("/") || prevTerm.equals("("))) {
                return "";
            }

            calcExpression.add(fragment);
            tempNum = ""; //clear temp num

            if (fragment.equals("*")) {
                return "×";
            }

            if (fragment.equals("/")) {
                return "÷";
            }

            System.out.println(calcExpression);

            return fragment; //stop method from proceding further
        }

        String prevTerm = calcExpression.size() - 1 > -1 ? calcExpression.get(calcExpression.size() - 1) : "+";

        if (isNumeric(prevTerm)) {
            calcExpression.clear();
            tempNum += fragment;
            return fragment;
        }

        tempNum += fragment;
        System.out.println(calcExpression);
        return fragment;
    }

    public void clearExpression() {
        calcExpression.clear();
        tempNum = "";
    }

    public void deleteLastCharacter() {
        //delete last character of number currently being typed
        if (!tempNum.equals("")) {
            tempNum = tempNum.substring(0, tempNum.length() - 1);
            return;
        }

        //if there are no characters to delete
        if (calcExpression.size() - 1 == -1) {
            return;
        }

        //previous term
        int index = calcExpression.size() - 1;

        String previousElement = calcExpression.get(index);

        //to remove the element if further removal is not possible
        if (previousElement.length() == 1) {
            calcExpression.remove(index);
            return;
        }

        //remove last character of string
        calcExpression.set(index, previousElement.replaceAll(".$", ""));

    }

    public java.lang.Double computeExpression() {
        if(backParenthasesNeededCount != 0) {
            return null;
        }
        
        if (!tempNum.equals("")) {
            calcExpression.add(tempNum);
        }

        String prevTerm = calcExpression.size() - 1 > -1 ? calcExpression.get(calcExpression.size() - 1) : "";

        if (prevTerm.equals("+") || prevTerm.equals("-") || prevTerm.equals("*") || prevTerm.equals("/")) {
            return null;
        }

        double finalOutput = parseExpression(calcExpression);

        clearExpression();

        currentOutput = finalOutput;

        calcExpression.add(Double.toString(finalOutput));

        return finalOutput;
    }

    public double parseExpression(ArrayList<String> list) { //list param is for parentheses parsing
        ArrayList<String> firstParse = new ArrayList<String>();
        ArrayList<String> secondParse = new ArrayList<String>();

        double finalOutput = 0;

        int parentheses1Idx = -1;
        int parentheses2Idx = -1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("(")) {
                parentheses1Idx = i;
            }

            if (list.get(i).equals(")")) {
                ArrayList<String> tempList = new ArrayList<String>(); //for storing the items in the parenthases to calculate
                parentheses2Idx = i;

                //add the parenthases items
                for (i = parentheses1Idx + 1; i < parentheses2Idx; i++) {
                    tempList.add(list.get(i));
                }

                double output = parseExpression(tempList);

                //to replace the parrenthases and its items with the output for further parsing
                list.set(parentheses1Idx, Double.toString(output));
                list.subList(parentheses1Idx + 1, parentheses2Idx + 1).clear();
            }
        }

        //first for multiplying
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("*")) {
                double prevNum = Double.parseDouble(list.get(i - 1));
                double nextNum = Double.parseDouble(list.get(i + 1));

                String sign = (i - 2) > -1 ? list.get(i - 2) : "";

                double multipliedTerm = prevNum * nextNum;

                if (sign.equals("-")) {
                    multipliedTerm = -(multipliedTerm);
                }

                firstParse.remove(firstParse.size() - 1); //remove element before multiplication symbol

                //to remove the 'add' or 'subtract' symbols, so that it is easier to calculate in a later step
                if ((firstParse.size() - 1) > -1 && (firstParse.get(firstParse.size() - 1).equals("+") || firstParse.get(firstParse.size() - 1).equals("-"))) {
                    firstParse.remove(firstParse.size() - 1);
                }

                firstParse.add(Double.toString(multipliedTerm));
                continue;
            }

            if ((i - 1) > -1 && list.get(i - 1).equals("*")) {
                continue;
            }

            firstParse.add(list.get(i));
        }

        //,then for dividing
        for (int i = 0; i < firstParse.size(); i++) {
            if (firstParse.get(i).equals("/")) {
                double prevNum = Double.parseDouble(firstParse.get(i - 1));
                double nextNum = Double.parseDouble(firstParse.get(i + 1));

                String sign = i > 2 ? list.get(i - 2) : "";

                double dividedTerm = prevNum / nextNum;

                if (sign.equals("-")) {
                    dividedTerm = -(dividedTerm);
                }

                secondParse.remove(secondParse.size() - 1); //remove element before multiplication symbol

                //to remove the 'add' or 'subtract' symbols, so that it is easier to calculate in a later step
                if ((secondParse.size() - 1) > -1 && (secondParse.get(secondParse.size() - 1).equals("+") || secondParse.get(secondParse.size() - 1).equals("-"))) {
                    secondParse.remove(secondParse.size() - 1);
                }

                secondParse.add(Double.toString(dividedTerm));
                continue;
            }

            if ((i - 1) > -1 && firstParse.get(i - 1).equals("/")) {
                continue;
            }

            secondParse.add(firstParse.get(i));
        }

        for (int i = 0; i < secondParse.size(); i++) {

            if (secondParse.get(i).equals("+")) {
                secondParse.remove(i);
            }

            if (secondParse.get(i).equals("-")) {
                double negatedNum = -(Double.parseDouble(secondParse.get(i + 1)));

                secondParse.set((i + 1), Double.toString(negatedNum));

                secondParse.remove(i);
            }

        }

        //for the final output
        for (String num : secondParse) {
            finalOutput += (Double.parseDouble(num));
        }

        return finalOutput;
    }

    public void setMemNum() {
        memNum = currentOutput;
    }

    public void deleteMemNum() {
        memNum = 0;
    }

    public String addMemToExpression() {
        //if(fragment.equals("+") || fragment.equals("-") || fragment.equals("*") || fragment.equals("/") || fragment.equals("%")) {

        //}
        if (memNum == 0.0) {
            return "";
        }

        if (!tempNum.equals("")) {
            return "";
        }

        addToExpression(Double.toString(memNum));
        return Double.toString(memNum);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    public String getPrevTerm() {
        String prevTerm = calcExpression.size() - 1 > -1 ? calcExpression.get(calcExpression.size() - 1) : "";
        return prevTerm;
    }

    public boolean isPrevTermNumber() {
        String prevTerm = getPrevTerm();
        boolean isNum = isNumeric(prevTerm);
        return isNum;
    }
}
