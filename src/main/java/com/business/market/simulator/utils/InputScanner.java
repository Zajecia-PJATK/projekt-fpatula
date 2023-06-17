package com.business.market.simulator.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Getter
@Component
public class InputScanner {
    private final Scanner scanner;

    public InputScanner() {
        scanner = new Scanner(System.in);
    }

    public Scanner getScanner() {
        return scanner;
    }
}
