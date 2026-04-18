Goal:
Develop and Operate a Web Application for Petitions (creating, viewing, and signing petitions)
following DevOps automation mechanisms.
Requirements:
1. Web App:
- Name of the Project: <your_first_name>spetitions
  o E.g.,: If you name is John: johnspetitions
- Start with Spring Boot initialiser with Maven, and Java 21
- Create at least:
  o 1 page to create a petition
  o 1 page to view all petitions
  o 1 page to search for a petition + 1 page with the result
  o Each petition could be clicked
  ▪ 1 page to view the petition and sign it (with name and email)
  o It is not mandatory to use a database
  ▪ You could use Java data structures (e.g., lists) to store the data.
  ▪ Put some random data in them at the start.
2. Development
- Use GitHub as the Version Control System
  o Have at least 1 commits per feature (page in the web application)
  o Storing Jenkinsfile in Github
  o Configuring GitHub Webhook
- NOT MANDATORY: Following Test Driven Development
  o For each feature, have at least 2 commits (1 for unit tests and one for code)
3. Continuous Deployment
   a. With Jenkins CI/CD pipelines (using Jenkinsfile)
   b. Running on Amazon EC2
   c. Automatically run after each commit
   d. With at least the following stages:
   i. Get the code from Github
   ii. Build
   iii. Test
   iv. Package and archive as a War file
   • named <your_first_name>spetitions.war
   e.g., johnspetition.war
   v. (re-)Deploy, if the user manually approves it
   CT5209 CA1: Continuous Delivery of a Web Application Takfarinas Saber
   2
   • as a Tomcat container
   • On Amazon EC2
   • Publicly accessible at http://SOME_EC2_PUBLIC_IP:9090
   General Instructions
   This CA individual and it is worth 20%.
   What to submit?
   You will need to submit a zip file that contains the following:
1. A PDF Report
   The report you write should be clear, well organized and at a professional standard. This is an
   indicative structure that you can follow:
   • Front Page:
   • title of your report
   • Your name, student ID number, email address,
   • module code
   • Link to your Github repository
   • Should be a public repository.
   • Link to your Web Application on Amazon EC2
   • Should be accessible from anywhere.
   • Description and organization of your web application.
   • Reflection on the challenges
   • What was hardest in the project?
   • List of requirements that you have not (fully) covered.
   • How you might try to cover them if given more time
   • Any appendix
   The total number of pagers for this report is a maximum of 5 pages (excluding the appendix, the
   Front page). This is a maximum and not a target.
2. A 5 Mins (Max) video demo: showing and explaining the working of your
   implementation/pipeline.
   • You need to particularly show the full process in action:
   i. The Web application before change
   ii. change, commit, automatic build,
   iii. asking the developer if they want to deploy,
   iv. The Web application after change
   Marking scheme:
   CT5209 CA1: Continuous Delivery of a Web Application Takfarinas Saber
   3
   Item Mark
   (/100)
   Fail (<40%) Pass (40%--59%) Excellent (>60%)
   Req 1 20% No web application or
   only the Spring Boot
   initializer
   Web application
   implementing most of
   the features
   Web application with
   all the described
   features
   Req 2 20% No use of Github. Use of Github but very
   few commits.
   Using Github with
   enough commits to
   show the iterative
   development.
   Configuration of
   Webhook.
   Req 3 40% No pipeline or very
   limited pipeline
   Pipeline with most
   stages
   Pipeline with all
   stages.
   Automatically run after
   commit.
   Automatic
   Deployment after
   confirmation.
   Report
   and
   Code
   Quality
   10% No report is provided,
   the report is poorly
   written, or the report
   does not follow the
   required structure.
   Links to Github/EC2 not
   provided.
   The report is provided,
   well structured, covers
   all the required sections
   but contains some
   inaccuracies,
   spelling/grammatical
   errors typos.
   Link to code provide, but
   code/tests are not well
   documented.
   Not reflection on
   challenges and non-
   implemented features.
   The report is at a
   professional level: well
   structured, has no
   inaccuracies, errors.
   Link to Github
   provided and
   code/tests well
   documented.
   Good reflection on
   challenges
   Video
   Demo
   10% No video demo or video
   of a very poor quality is
   provided. Video not
   describing the
   implementation and the
   working of the
   application.
   Video demo is provided,
   but the description of
   the implementation and
   working of the
   application and CI/CD
   are not detailed/clear.
   Video demo is
   provided. The video
   demo provides a clear
   overview of the
   implementation and
   showcases the
   functioning of the
   application and CI/CD
   pipeline.
