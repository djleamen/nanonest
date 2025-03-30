# System Test

## ST-01-OB
### Testing Different Authentication Levels
### :white_check_mark: Passed
1. Sign in as user
2. Confirm correct authentication level :white_check_mark:
3. Sign in as admin
4. Confirm correct authentication level :white_check_mark:
5. Sign in with invalid information
6. Confirm log in is denied :white_check_mark:

## ST-02-OB
### Testing Data Persistance After Restart
### :x: Failed
1. Modify CSV data
2. Restart application
3. Display data and confirm change stayed

## ST-03-OB
### Testing UI Navigation and Menu Interactions
### :white_check_mark: Passed
1. Navigate through UI menus 
2. Select and use all of the different operations 
3. Return to the main menu


## ST-04-TB
### Testing Proper Relevancy Search Results
### :white_circle: Untested (Not Implemented Fully Yet)
1. Perform a relevancy search
2. View the search results and check to see the accuracy of the rankings


## ST-05-CB
### Testing File Input/Output Exception Handling
### :x: Failed
1. Load a non-existent CSV file
2. Attempt to write to a read-only file


