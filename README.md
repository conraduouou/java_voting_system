# java_voting_system
A simple voting system made on Java GUI. Specifically made for ModSim presentation.


## Steps to get it to work:
1. Pumunta sa VotingDatabaseCreate.java, tapos i-run ito. Palitan yung necessary credentials (USER, PASS) na nasa bandang taas, mga static variables ito ng class at naka-depende sa credentials ng machine ninyo, specifically, ng database niyo. Kung naka-XAMPP or mySQL kayo, gagana naman 'yan.
2. I-uncomment ang createDatabase method at i-comment naman yung createTables na method sa loob ng main method. Nasa bandang line 28 at 29 ito. I-run.
3. I-uncomment naman ang createTables at i-comment ang createDatabase. I-run muli.
4. Kung successful naman ang pag-run, dapat mayroon na kayong database na may default admin account na username:root at password:pass. 


## Run the VoteLogin.java file and it should work as intended
