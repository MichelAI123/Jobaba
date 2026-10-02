package com.example.data.model

data class CandidateProfile(
    val name: String = "Michel DONGMO",
    val email: String = "michel.dongmoza@gmail.com",
    val phone: String = "+1 (613) 555-0198",
    val location: String = "Ottawa, Ontario, Canada",
    val headline: String = "Intermediate AI/MLSecOps Engineer — Applied AI, Computer Vision & LLM/VLM Profile",
    val professionalSummary: String = "Applied AI and Machine Learning Engineer specializing in Computer Vision (YOLOv8, OpenCV, ALPR), LLM/VLM orchestration (RAG, MCP), and containerized MLOps pipelines (Docker, MLflow, FastAPI). Strong mathematical foundation with ongoing Ph.D research in Mathematical Physics, delivering production-grade inference systems with verified metrics.",
    val verifiedSkills: List<String> = listOf(
        "Python", "PyTorch", "TensorFlow", "YOLOv8", "OpenCV", "EasyOCR",
        "FastAPI", "MLflow", "Docker", "Docker Compose", "RAG", "MCP",
        "LLM/VLM Orchestration", "Scikit-learn", "XGBoost", "Pandas", "NumPy",
        "C++", "Linux/Ubuntu", "PostgreSQL", "SQL", "MATLAB", "Simulation & Optimization"
    ),
    val projects: List<VerifiedProject> = listOf(
        VerifiedProject(
            id = "PRJ-SIACP",
            title = "SIACP — Intelligent Parking Control & ALPR System",
            technologies = listOf("Python", "YOLOv8", "EasyOCR", "OpenCV", "FastAPI", "Streamlit", "SQLite", "MLflow", "Docker", "Docker Compose"),
            description = "Production-grade automated license plate recognition and vehicle access control system.",
            achievements = listOf(
                "Implemented real-time vehicle detection and ALPR using custom fine-tuned YOLOv8 and EasyOCR image preprocessing pipelines.",
                "Engineered scalable REST APIs using FastAPI with JWT authorization and automated SQLite audit logging.",
                "Integrated MLflow experiment tracking to benchmark model confidence thresholds and inference latency.",
                "Containerized entire multi-service architecture using Docker and Docker Compose for zero-downtime deployment."
            )
        ),
        VerifiedProject(
            id = "PRJ-AIR-QUALITY",
            title = "Air Quality Prediction & ML Inference API",
            technologies = listOf("Python", "Scikit-learn", "Random Forest", "XGBoost", "Pandas", "NumPy", "FastAPI", "Docker", "Joblib"),
            description = "Predictive environmental machine learning service predicting particulate matter indices with serialized model inference.",
            achievements = listOf(
                "Developed end-to-end ML pipeline with extensive feature engineering and cross-validation, achieving verified F1-score of 0.8284.",
                "Deployed high-throughput asynchronous inference endpoint in FastAPI using serialized Joblib model artifacts.",
                "Packaged application in lightweight Docker container with automated health checks and logging."
            )
        )
    ),
    val education: List<EducationEntry> = listOf(
        EducationEntry(
            degree = "Ongoing Ph.D Research in Mathematical Physics",
            institution = "University of Dschang",
            period = "Ongoing",
            details = "Polarons-Solitons and their applications in AI Tech and Computational Modelling."
        ),
        EducationEntry(
            degree = "Collegiate Diploma — Artificial Intelligence in Informatics",
            institution = "Collège La Cité, Ottawa, Canada",
            period = "2024 – 2026",
            details = "High Distinction — GPA 4.02 / 4.3. Applied AI, Deep Learning, Edge Computing & Cloud DevOps."
        ),
        EducationEntry(
            degree = "Master's Degree — Condensed Matter and Mathematical Physics",
            institution = "University of Dschang",
            period = "2014 – 2017",
            details = "Analytical & numerical simulation, statistical physics, differential equations, and nonlinear dynamics."
        ),
        EducationEntry(
            degree = "Bachelor's Degree — General Physics and Programming",
            institution = "University of Dschang",
            period = "2009 – 2013",
            details = "Scientific computing, C++, algorithm design, and applied mathematics."
        ),
        EducationEntry(
            degree = "Bachelor's Degree — Education and Teaching",
            institution = "University of Bamenda",
            period = "2008 – 2012",
            details = "Pedagogical methodologies, technical instruction, and curriculum development."
        )
    ),
    val experience: List<ExperienceEntry> = listOf(
        ExperienceEntry(
            title = "Technical Instructor / Software Troubleshooter",
            company = "La Cité Collégiale — Ottawa, Canada",
            period = "2024 – 2025",
            bullets = listOf(
                "Delivered technical curriculum in programming and software troubleshooting across virtual and LMS platforms.",
                "Guided students in AI tool integration including NotebookLM, modern IDE workflows, and algorithmic debugging.",
                "Provided specialized software environment setup and Linux system diagnostics for departmental labs."
            )
        ),
        ExperienceEntry(
            title = "Technical Advisor and Modeling Specialist",
            company = "Centre Polyvalent de Formation — Cameroon",
            period = "2021 – 2023",
            bullets = listOf(
                "Architected embedded C++ and IoT sensor telemetry systems using Arduino microcontrollers.",
                "Formulated predictive mathematical models and numerical simulations for operational forecasting.",
                "Optimized multi-variable computational algorithms, accelerating processing times by 35%."
            )
        ),
        ExperienceEntry(
            title = "Science Lead and Technical Coordinator",
            company = "Ministry of Secondary Education — Cameroon",
            period = "2013 – 2023",
            bullets = listOf(
                "Directed scientific laboratory operations, experimental safety protocols, and technical alignment across regional institutions.",
                "Enforced standardized measurement methodologies and quality assurance guidelines for STEM curricula."
            )
        )
    )
)

data class VerifiedProject(
    val id: String,
    val title: String,
    val technologies: List<String>,
    val description: String,
    val achievements: List<String>
)

data class EducationEntry(
    val degree: String,
    val institution: String,
    val period: String,
    val details: String
)

data class ExperienceEntry(
    val title: String,
    val company: String,
    val period: String,
    val bullets: List<String>
)
