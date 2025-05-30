from gradientai import Gradient
import os
from flask import Flask, request, jsonify
import re
import random

# Set your credentials
token = 'LXkZBp5a1nGCP16xM7QieYKNz2Ns0tOW'
workspace_id = 'b5f958d9-ffd4-41bb-a492-704114e02c8e_workspace'

# Environment config
os.environ['GRADIENT_ACCESS_TOKEN'] = token
os.environ['GRADIENT_WORKSPACE_ID'] = workspace_id

app = Flask(__name__)

# Gradient client
gradient = Gradient()

# Dummy interest-based question bank
dummy_questions = {
    "Java": [
        {
            "question": "What is Java?",
            "options": [
                "A fruit",
                "A programming language",
                "A planet",
                "A city"
            ],
            "correct_answer": "A programming language",
            "topic": "Java"
        },
        {
            "question": "Which keyword is used to inherit a class in Java?",
            "options": [
                "extends",
                "inherits",
                "instanceof",
                "implements"
            ],
            "correct_answer": "extends",
            "topic": "Java"
        },
        {
            "question": "Which company originally developed Java?",
            "options": [
                "Oracle",
                "Sun Microsystems",
                "Microsoft",
                "IBM"
            ],
            "correct_answer": "Sun Microsystems",
            "topic": "Java"
        },
        {
            "question": "Which method is the entry point of a Java program?",
            "options": [
                "main()",
                "start()",
                "init()",
                "run()"
            ],
            "correct_answer": "main()",
            "topic": "Java"
        },
        {
            "question": "Which file extension is used for compiled Java classes?",
            "options": [
                ".java",
                ".class",
                ".jar",
                ".exe"
            ],
            "correct_answer": ".class",
            "topic": "Java"
        },
        {
            "question": "Which Java keyword is used to create a new object?",
            "options": [
                "new",
                "create",
                "object",
                "init"
            ],
            "correct_answer": "new",
            "topic": "Java"
        }
    ],
    "Python": [
        {
            "question": "Which symbol starts a comment in Python?",
            "options": [
                "#",
                "//",
                "/*",
                "--"
            ],
            "correct_answer": "#",
            "topic": "Python"
        },
        {
            "question": "Which function outputs text in Python?",
            "options": [
                "print()",
                "output()",
                "echo()",
                "say()"
            ],
            "correct_answer": "print()",
            "topic": "Python"
        },
        {
            "question": "What is the correct way to create a list in Python?",
            "options": [
                "list = {}",
                "list = []",
                "list = ()",
                "list = <>"
            ],
            "correct_answer": "list = []",
            "topic": "Python"
        },
        {
            "question": "What does 'def' do in Python?",
            "options": [
                "Defines a function",
                "Creates a loop",
                "Imports a module",
                "Declares a variable"
            ],
            "correct_answer": "Defines a function",
            "topic": "Python"
        },
        {
            "question": "What type is the result of 3 / 2 in Python 3?",
            "options": [
                "int",
                "float",
                "str",
                "bool"
            ],
            "correct_answer": "float",
            "topic": "Python"
        },
        {
            "question": "Which Python keyword is used for exception handling?",
            "options": [
                "catch",
                "try",
                "error",
                "raise"
            ],
            "correct_answer": "try",
            "topic": "Python"
        }
    ],
    "Web Development": [
        {
            "question": "What does HTML stand for?",
            "options": [
                "Hyper Text Markup Language",
                "Home Tool Markup Language",
                "Hyper Tool Multi Language",
                "Hyperlinks and Text Markup Language"
            ],
            "correct_answer": "Hyper Text Markup Language",
            "topic": "Web Development"
        },
        {
            "question": "What is used to style websites?",
            "options": [
                "JavaScript",
                "CSS",
                "Python",
                "SQL"
            ],
            "correct_answer": "CSS",
            "topic": "Web Development"
        },
        {
            "question": "Which language makes websites interactive?",
            "options": [
                "CSS",
                "JavaScript",
                "HTML",
                "XML"
            ],
            "correct_answer": "JavaScript",
            "topic": "Web Development"
        },
        {
            "question": "What does the <a> tag do?",
            "options": [
                "Adds a link",
                "Creates a paragraph",
                "Inserts an image",
                "Creates a table"
            ],
            "correct_answer": "Adds a link",
            "topic": "Web Development"
        },
        {
            "question": "Which property is used in CSS to change text color?",
            "options": [
                "color",
                "font-color",
                "text-color",
                "background-color"
            ],
            "correct_answer": "color",
            "topic": "Web Development"
        },
        {
            "question": "Which language runs in the browser?",
            "options": [
                "Java",
                "Python",
                "JavaScript",
                "C#"
            ],
            "correct_answer": "JavaScript",
            "topic": "Web Development"
        }
    ],
    "AI": [
        {
            "question": "What is the full form of AI?",
            "options": [
                "Automated Interaction",
                "Artificial Intelligence",
                "Analytical Interface",
                "Artificial Integration"
            ],
            "correct_answer": "Artificial Intelligence",
            "topic": "AI"
        },
        {
            "question": "Which of these is a subset of AI?",
            "options": [
                "Machine Learning",
                "Web Development",
                "Database Design",
                "Data Entry"
            ],
            "correct_answer": "Machine Learning",
            "topic": "AI"
        },
        {
            "question": "What is an example of AI in everyday life?",
            "options": [
                "Smartphone voice assistant",
                "Textbook",
                "Mouse",
                "Paper"
            ],
            "correct_answer": "Smartphone voice assistant",
            "topic": "AI"
        },
        {
            "question": "Which of the following is a popular AI framework?",
            "options": [
                "TensorFlow",
                "Photoshop",
                "Excel",
                "After Effects"
            ],
            "correct_answer": "TensorFlow",
            "topic": "AI"
        },
        {
            "question": "Which programming language is most often used in AI?",
            "options": [
                "Python",
                "HTML",
                "PHP",
                "Swift"
            ],
            "correct_answer": "Python",
            "topic": "AI"
        },
        {
            "question": "Which AI feature allows computers to learn from data?",
            "options": [
                "Machine Learning",
                "Typing",
                "Rendering",
                "Styling"
            ],
            "correct_answer": "Machine Learning",
            "topic": "AI"
        }
    ],
    "Algorithms": [
        {
            "question": "What is the time complexity of binary search?",
            "options": [
                "O(n)",
                "O(log n)",
                "O(n^2)",
                "O(1)"
            ],
            "correct_answer": "O(log n)",
            "topic": "Algorithms"
        },
        {
            "question": "Which algorithm is used for sorting?",
            "options": [
                "Merge Sort",
                "Linear Search",
                "Dijkstra's",
                "BFS"
            ],
            "correct_answer": "Merge Sort",
            "topic": "Algorithms"
        },
        {
            "question": "Which algorithm finds shortest paths in a graph?",
            "options": [
                "DFS",
                "Prim's",
                "Dijkstra's",
                "Quick Sort"
            ],
            "correct_answer": "Dijkstra's",
            "topic": "Algorithms"
        },
        {
            "question": "Which sorting algorithm is known for being fast on average?",
            "options": [
                "Quick Sort",
                "Bubble Sort",
                "Linear Search",
                "DFS"
            ],
            "correct_answer": "Quick Sort",
            "topic": "Algorithms"
        },
        {
            "question": "Which algorithm uses divide and conquer?",
            "options": [
                "Merge Sort",
                "Binary Search",
                "BFS",
                "Greedy"
            ],
            "correct_answer": "Merge Sort",
            "topic": "Algorithms"
        },
        {
            "question": "What is the main purpose of a search algorithm?",
            "options": [
                "Find items in data",
                "Sort items",
                "Create files",
                "Draw images"
            ],
            "correct_answer": "Find items in data",
            "topic": "Algorithms"
        }
    ],
    "Data Structures": [
        {
            "question": "Which data structure uses FIFO?",
            "options": [
                "Queue",
                "Stack",
                "Tree",
                "Graph"
            ],
            "correct_answer": "Queue",
            "topic": "Data Structures"
        },
        {
            "question": "Which data structure is used in recursion?",
            "options": [
                "Queue",
                "Array",
                "Stack",
                "Linked List"
            ],
            "correct_answer": "Stack",
            "topic": "Data Structures"
        },
        {
            "question": "Which data structure has nodes and children?",
            "options": [
                "Tree",
                "Array",
                "Stack",
                "Queue"
            ],
            "correct_answer": "Tree",
            "topic": "Data Structures"
        },
        {
            "question": "Which structure uses LIFO (Last In First Out)?",
            "options": [
                "Stack",
                "Queue",
                "Graph",
                "Tree"
            ],
            "correct_answer": "Stack",
            "topic": "Data Structures"
        },
        {
            "question": "Which data structure is made up of nodes and pointers?",
            "options": [
                "Linked List",
                "Array",
                "HashMap",
                "Queue"
            ],
            "correct_answer": "Linked List",
            "topic": "Data Structures"
        },
        {
            "question": "Which structure stores data in key-value pairs?",
            "options": [
                "HashMap",
                "Array",
                "Stack",
                "Graph"
            ],
            "correct_answer": "HashMap",
            "topic": "Data Structures"
        }
    ],
    "UI/UX": [
        {
            "question": "What does UX stand for?",
            "options": [
                "User Experience",
                "User Expert",
                "Unified Experience",
                "Universal Experience"
            ],
            "correct_answer": "User Experience",
            "topic": "UI/UX"
        },
        {
            "question": "Which tool is commonly used to design wireframes?",
            "options": [
                "Figma",
                "MySQL",
                "IntelliJ",
                "Photoshop"
            ],
            "correct_answer": "Figma",
            "topic": "UI/UX"
        },
        {
            "question": "What is the goal of UX design?",
            "options": [
                "User satisfaction",
                "Backend performance",
                "Styling",
                "Security"
            ],
            "correct_answer": "User satisfaction",
            "topic": "UI/UX"
        },
        {
            "question": "What does UI stand for?",
            "options": [
                "User Interface",
                "Unique Identity",
                "Universal Input",
                "Unified Instance"
            ],
            "correct_answer": "User Interface",
            "topic": "UI/UX"
        },
        {
            "question": "Which of these is a design principle in UX?",
            "options": [
                "Usability",
                "Multithreading",
                "Encryption",
                "Recursion"
            ],
            "correct_answer": "Usability",
            "topic": "UI/UX"
        },
        {
            "question": "Which color tool helps design UI themes?",
            "options": [
                "Adobe Color",
                "Python",
                "SQL",
                "MongoDB"
            ],
            "correct_answer": "Adobe Color",
            "topic": "UI/UX"
        }
    ],
    "Databases": [
        {
            "question": "What does SQL stand for?",
            "options": [
                "Structured Query Language",
                "Simple Query Logic",
                "Sequential Query Language",
                "System Query Layer"
            ],
            "correct_answer": "Structured Query Language",
            "topic": "Databases"
        },
        {
            "question": "Which of the following is a NoSQL database?",
            "options": [
                "MongoDB",
                "MySQL",
                "PostgreSQL",
                "Oracle"
            ],
            "correct_answer": "MongoDB",
            "topic": "Databases"
        },
        {
            "question": "Which SQL command is used to retrieve data?",
            "options": [
                "GET",
                "SELECT",
                "SHOW",
                "FETCH"
            ],
            "correct_answer": "SELECT",
            "topic": "Databases"
        },
        {
            "question": "Which command is used to insert data in SQL?",
            "options": [
                "INSERT",
                "ADD",
                "WRITE",
                "PUSH"
            ],
            "correct_answer": "INSERT",
            "topic": "Databases"
        },
        {
            "question": "What type of database is MySQL?",
            "options": [
                "Relational",
                "NoSQL",
                "Document",
                "Graph"
            ],
            "correct_answer": "Relational",
            "topic": "Databases"
        },
        {
            "question": "Which database is used for mobile apps?",
            "options": [
                "SQLite",
                "Access",
                "Oracle",
                "MongoSQL"
            ],
            "correct_answer": "SQLite",
            "topic": "Databases"
        }
    ],
    "Testing": [
        {
            "question": "What is unit testing?",
            "options": [
                "Testing individual units/components",
                "Testing UI",
                "Testing the whole system",
                "Performance testing"
            ],
            "correct_answer": "Testing individual units/components",
            "topic": "Testing"
        },
        {
            "question": "Which is a common testing framework in Java?",
            "options": [
                "JUnit",
                "Mocha",
                "RSpec",
                "NUnit"
            ],
            "correct_answer": "JUnit",
            "topic": "Testing"
        },
        {
            "question": "Which type of testing ensures software performs under load?",
            "options": [
                "Load testing",
                "Unit testing",
                "UI testing",
                "Beta testing"
            ],
            "correct_answer": "Load testing",
            "topic": "Testing"
        },
        {
            "question": "What is integration testing?",
            "options": [
                "Testing combined modules",
                "Testing design",
                "Testing CSS",
                "Testing passwords"
            ],
            "correct_answer": "Testing combined modules",
            "topic": "Testing"
        },
        {
            "question": "What is a test case?",
            "options": [
                "A set of inputs and expected results",
                "A CSS file",
                "A user profile",
                "An app icon"
            ],
            "correct_answer": "A set of inputs and expected results",
            "topic": "Testing"
        },
        {
            "question": "Which tool is used for automated testing?",
            "options": [
                "Selenium",
                "React",
                "MongoDB",
                "Postman"
            ],
            "correct_answer": "Selenium",
            "topic": "Testing"
        }
    ],
    "Cybersecurity": [
        {
            "question": "What is phishing?",
            "options": [
                "A hacking technique",
                "A programming error",
                "A database type",
                "A security patch"
            ],
            "correct_answer": "A hacking technique",
            "topic": "Cybersecurity"
        },
        {
            "question": "What is a strong password?",
            "options": [
                "123456",
                "admin",
                "P@ssw0rd!",
                "password"
            ],
            "correct_answer": "P@ssw0rd!",
            "topic": "Cybersecurity"
        },
        {
            "question": "Which of the following is a common cybersecurity threat?",
            "options": [
                "Malware",
                "Bluetooth",
                "SSD",
                "Keyboard"
            ],
            "correct_answer": "Malware",
            "topic": "Cybersecurity"
        },
        {
            "question": "What is encryption?",
            "options": [
                "Converting data to secure form",
                "Deleting files",
                "Making backups",
                "Sorting data"
            ],
            "correct_answer": "Converting data to secure form",
            "topic": "Cybersecurity"
        },
        {
            "question": "What does a firewall do?",
            "options": [
                "Blocks unauthorized access",
                "Prints documents",
                "Fixes errors",
                "Manages tasks"
            ],
            "correct_answer": "Blocks unauthorized access",
            "topic": "Cybersecurity"
        },
        {
            "question": "What is a common sign of phishing?",
            "options": [
                "Fake emails asking for info",
                "Fast internet",
                "Phone updates",
                "Discount codes"
            ],
            "correct_answer": "Fake emails asking for info",
            "topic": "Cybersecurity"
        }
    ],
    "Operating Systems": [
        {
            "question": "Which OS is developed by Microsoft?",
            "options": [
                "Windows",
                "Linux",
                "macOS",
                "Ubuntu"
            ],
            "correct_answer": "Windows",
            "topic": "Operating Systems"
        },
        {
            "question": "Which of the following is an open-source OS?",
            "options": [
                "Linux",
                "Windows",
                "iOS",
                "macOS"
            ],
            "correct_answer": "Linux",
            "topic": "Operating Systems"
        },
        {
            "question": "What does GUI stand for?",
            "options": [
                "Graphical User Interface",
                "General User Interface",
                "Group User Input",
                "Global Utility Interface"
            ],
            "correct_answer": "Graphical User Interface",
            "topic": "Operating Systems"
        },
        {
            "question": "Which OS is mainly used in Apple computers?",
            "options": [
                "macOS",
                "Windows",
                "Ubuntu",
                "Fedora"
            ],
            "correct_answer": "macOS",
            "topic": "Operating Systems"
        },
        {
            "question": "What is the core of an operating system called?",
            "options": [
                "Kernel",
                "Shell",
                "BIOS",
                "Processor"
            ],
            "correct_answer": "Kernel",
            "topic": "Operating Systems"
        },
        {
            "question": "Which OS is commonly used in servers?",
            "options": [
                "Linux",
                "Android",
                "iOS",
                "Chrome OS"
            ],
            "correct_answer": "Linux",
            "topic": "Operating Systems"
        }
    ],
    "Software Engineering": [
        {
            "question": "What is the first phase of the SDLC?",
            "options": [
                "Requirements gathering",
                "Coding",
                "Testing",
                "Deployment"
            ],
            "correct_answer": "Requirements gathering",
            "topic": "Software Engineering"
        },
        {
            "question": "Which model involves repeating cycles of development?",
            "options": [
                "Agile",
                "Waterfall",
                "V-Shaped",
                "Spiral"
            ],
            "correct_answer": "Agile",
            "topic": "Software Engineering"
        },
        {
            "question": "What does UML stand for?",
            "options": [
                "Unified Modeling Language",
                "Universal Module Language",
                "Unique Machine Logic",
                "User Model Layout"
            ],
            "correct_answer": "Unified Modeling Language",
            "topic": "Software Engineering"
        },
        {
            "question": "Which document defines software requirements?",
            "options": [
                "SRS",
                "UML",
                "SLA",
                "API"
            ],
            "correct_answer": "SRS",
            "topic": "Software Engineering"
        },
        {
            "question": "What is version control used for?",
            "options": [
                "Tracking code changes",
                "Compiling programs",
                "Writing documentation",
                "Designing UI"
            ],
            "correct_answer": "Tracking code changes",
            "topic": "Software Engineering"
        },
        {
            "question": "Which of these is a popular version control tool?",
            "options": [
                "Git",
                "Jira",
                "Postman",
                "Eclipse"
            ],
            "correct_answer": "Git",
            "topic": "Software Engineering"
        }
    ],
    "Mobile App Development": [
        {
            "question": "Which language is used for Android development?",
            "options": [
                "Java",
                "Python",
                "Swift",
                "Ruby"
            ],
            "correct_answer": "Java",
            "topic": "Mobile App Development"
        },
        {
            "question": "Which platform is used to build iOS apps?",
            "options": [
                "Xcode",
                "Android Studio",
                "IntelliJ",
                "Visual Studio"
            ],
            "correct_answer": "Xcode",
            "topic": "Mobile App Development"
        },
        {
            "question": "Which file type is used to install Android apps?",
            "options": [
                ".apk",
                ".exe",
                ".ios",
                ".dmg"
            ],
            "correct_answer": ".apk",
            "topic": "Mobile App Development"
        },
        {
            "question": "Which layout is used in Android XML?",
            "options": [
                "LinearLayout",
                "Flexbox",
                "FloatLayout",
                "Grid"
            ],
            "correct_answer": "LinearLayout",
            "topic": "Mobile App Development"
        },
        {
            "question": "What is an Activity in Android?",
            "options": [
                "A screen",
                "A folder",
                "A database",
                "A style"
            ],
            "correct_answer": "A screen",
            "topic": "Mobile App Development"
        },
        {
            "question": "Which programming language is also used in Flutter?",
            "options": [
                "Dart",
                "Swift",
                "Go",
                "Rust"
            ],
            "correct_answer": "Dart",
            "topic": "Mobile App Development"
        }
    ],
    "APIs and Integration": [
        {
            "question": "What does API stand for?",
            "options": [
                "Application Programming Interface",
                "Advanced Program Integration",
                "Automated Processing Input",
                "App Performance Indicator"
            ],
            "correct_answer": "Application Programming Interface",
            "topic": "APIs and Integration"
        },
        {
            "question": "Which format is commonly used for API data?",
            "options": [
                "JSON",
                "PDF",
                "TXT",
                "DOC"
            ],
            "correct_answer": "JSON",
            "topic": "APIs and Integration"
        },
        {
            "question": "What is the purpose of an API key?",
            "options": [
                "Authenticate API requests",
                "Edit documents",
                "Send emails",
                "Compile code"
            ],
            "correct_answer": "Authenticate API requests",
            "topic": "APIs and Integration"
        },
        {
            "question": "Which tool is commonly used to test APIs?",
            "options": [
                "Postman",
                "Android Studio",
                "Xcode",
                "Eclipse"
            ],
            "correct_answer": "Postman",
            "topic": "APIs and Integration"
        },
        {
            "question": "Which method retrieves data in an API?",
            "options": [
                "GET",
                "POST",
                "DELETE",
                "UPDATE"
            ],
            "correct_answer": "GET",
            "topic": "APIs and Integration"
        },
        {
            "question": "What does REST stand for?",
            "options": [
                "Representational State Transfer",
                "Remote System Transfer",
                "Regional Service Transport",
                "Repeatable Software Testing"
            ],
            "correct_answer": "Representational State Transfer",
            "topic": "APIs and Integration"
        }
    ]
}



def fetchQuizFromLlama(student_topic):
    print("Fetching quiz from llama")
    base_model = gradient.get_base_model(base_model_slug="llama3-8b-chat")
    query = (
        f"[INST] Generate a quiz with 3 questions to test students on the provided topic. "
        f"For each question, generate 4 options where only one of the options is correct. "
        f"Format your response as follows:\n"
        f"QUESTION: [Your question here]?\n"
        f"OPTION A: [First option]\n"
        f"OPTION B: [Second option]\n"
        f"OPTION C: [Third option]\n"
        f"OPTION D: [Fourth option]\n"
        f"ANS: [Correct answer letter]\n\n"
        f"Ensure proper formatting. Topic:\n{student_topic}"
        f"[/INST]"
    )
    response = base_model.complete(query=query, max_generated_token_count=500).generated_output
    return response

def process_quiz(text, fallback_topic="General"):
    questions = []
    pattern = re.compile(
        r'QUESTION: (.*?)\nOPTION A: (.*?)\nOPTION B: (.*?)\nOPTION C: (.*?)\nOPTION D: (.*?)\nANS: (.)',
        re.DOTALL
    )
    matches = pattern.findall(text)

    for match in matches:
        question, a, b, c, d, ans = match
        options = [a.strip(), b.strip(), c.strip(), d.strip()]
        correct_answer = options[ord(ans.upper()) - ord('A')]
        random.shuffle(options)
        questions.append({
            "question": question.strip(),
            "options": options,
            "correct_answer": correct_answer,
            "topic": fallback_topic
        })

    return questions

@app.route('/getQuiz', methods=['GET'])
def get_quiz():
    topic = request.args.get('topic')
    if not topic:
        return jsonify({'error': 'No topic provided'}), 400

    try:
        raw_quiz = fetchQuizFromLlama(topic)
        return jsonify({'quiz': process_quiz(raw_quiz, topic)})

    except Exception as e:
        print(f"Gradient failed, returning dummy quiz: {e}")
        selected_interests = [i.strip() for i in topic.split(',')]
        fallback_questions = []

        for interest in selected_interests:
            if interest in dummy_questions:
                for q in dummy_questions[interest]:
                    q_with_topic = q.copy()
                    q_with_topic["topic"] = interest
                    options = q_with_topic["options"]
                    correct = q_with_topic["correct_answer"]

                    # Shuffle options and realign correct answer
                    random.shuffle(options)
                    q_with_topic["options"] = options
                    for opt in options:
                        if opt == correct:
                            q_with_topic["correct_answer"] = opt
                            break

                    fallback_questions.append(q_with_topic)

        random.shuffle(fallback_questions)
        quiz = fallback_questions[:6]
        return jsonify({'quiz': quiz}), 200

if __name__ == '__main__':
    app.run(port=5001, host='0.0.0.0')
