package com.viswagrid.util;

import java.util.Scanner;

public class CreateAdminHash {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a new admin password: ");
        String password = scanner.nextLine();

        String hashedPassword =
                PasswordUtil.hashPassword(password);

        System.out.println("\nGenerated BCrypt hash:");
        System.out.println(hashedPassword);

        scanner.close();
    }
}