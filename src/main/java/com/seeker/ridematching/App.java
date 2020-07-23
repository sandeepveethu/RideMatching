package com.seeker.ridematching;

import java.util.Scanner;

import com.seeker.ridematching.command.CommandRunner;
import com.seeker.ridematching.exception.InvalidCommandException;

/**
 * Ride Matching CLI Application
 *
 * @author sandeep
 */
public class App {
  public static void main(String[] args) {
    CommandRunner commandRunner = new CommandRunner();
    try (Scanner in = new Scanner(System.in)) {
      while (true) {
        if (in.hasNextLine()) {
          String command = in.nextLine();
          String result;
          try {
            result = commandRunner.runCommand(command);
            if (result != null) System.out.println(result);
          } catch (InvalidCommandException e) {
            System.err.println(e.getMessage());
          }
        } else {
          break;
        }
      }
    }
  }
}
