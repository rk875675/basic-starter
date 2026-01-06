# Bug: App crashes when `--name` has no value

## Steps to Reproduce
1. `mvn package`
2. `java -cp target/<jar>.jar com.rahul.App --name`

## Expected
No crash. Fall back to `Hello World!`.

## Actual
ArrayIndexOutOfBoundsException in parseName().

## Root Cause
Reads args[i + 1] without checking bounds.

## Fix
Added bounds check (i + 1 < args.length).
