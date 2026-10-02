# Canada Remote AI/ML Job Intelligence & ATS Command Center

A high-performance Android mobile command center for Canadian remote AI/ML job intelligence, ATS resume optimization, automated application workflows, and multi-modal Gemini AI copilot assistance tailored for Michel Dongmo.

## User Review & Critical Decisions

> [!IMPORTANT]
> The following user preferences were confirmed during clarification:
> - **Primary Architecture**: Unified command center with Job Discovery, ATS Studio, Market Analytics, and Live Gemini Copilot.
> - **Gemini AI Suite Priority**: Full multimodal AI capability, including High Thinking (`gemini-3.1-pro-preview`), Low-Latency Flash-Lite (`gemini-3.1-flash-lite`), Grounded Search & Maps (`gemini-3.5-flash`), Live Voice Copilot (`gemini-3.8-live`), Text-to-Speech (`gemini-3.8-flash-tts`), and High-Res Image Generation (`gemini-3-pro-image-preview`).
> - **Candidate Data & Listings**: Pre-loaded with Michel Dongmo's verified engineering profile (Computer Vision, LLM/VLM, MLOps, SIACP project) and a verified set of Canadian remote AI/ML opportunities exceeding CAD $80k–$90k+.

---

## 1. Overview & Core Concept

- **What It Does**: Empowers candidates and engineers to discover high-paying remote Canadian AI/ML opportunities, verify compliance gates (Remote Canada, CAD > $80k/$90k, relevant AI/ML stack), automatically optimize ATS resumes and cover letters with verifiable project evidence, track application stages end-to-end, and converse with a Gemini AI intelligence partner.
- **Target Audience / Persona**: AI/ML engineers, computer vision specialists, and computational researchers targeting the Canadian tech market who require precision matching, bulletproof ATS tailoring, and an executive mobile workspace.
- **Key Value**: Eliminates manual job tailoring friction, guarantees 100% factual fidelity against candidate truth, provides instant Gemini reasoning, and delivers real-time market intelligence on mobile.

---

## 2. User Experience & Visual Design

### Key User Flows
1. **Intelligence Dashboard & Quick Actions**: Users open the command center to view active market telemetry (Total Jobs, Verified Remote, Qualifying >$90k, Applications Submitted, Duplicate Prevention).
2. **Job Explorer & Gate Verification**: Browse, search, and filter Canadian remote AI/ML roles. Selecting any job opens the detailed Gate Check drawer (Remote verified, salary range, candidate match score, required tech vs. candidate evidence).
3. **ATS Document Studio**: For any qualifying vacancy, inspect the tailored Garamond-styled ATS CV preview and targeted 4-paragraph cover letter highlighting candidate projects (SIACP ALPR, Air Quality API). Export or copy text seamlessly.
4. **Application Command Center & Audit**: Track the 5 targeted applications through the lifecycle (`DISCOVERED` → `QUALIFYING` → `READY_TO_APPLY` → `SUBMITTED`) with confirmation timestamps, duplicate safeguards, and credential security.
5. **Gemini AI Intelligence & Live Audio Suite**:
   - *High-Thinking Copilot*: Deep reasoning on technical interview preparation, system design, and salary negotiations using `gemini-3.1-pro-preview` with `ThinkingLevel.HIGH`.
   - *Low-Latency Chat*: Rapid Q&A on company requirements and role fit using `gemini-3.1-flash-lite`.
   - *Grounded Search & Maps*: Verification of company offices, Canadian remote eligibility, and tech hiring news via `gemini-3.5-flash` with Search & Maps grounding.
   - *Live Audio / TTS*: Real-time spoken interview practice and audio playback of market briefings via `gemini-3.8-flash-tts`.
   - *Resume Infographics*: Generation of visual portfolio highlight cards using `gemini-3-pro-image-preview` (1K, 2K, 4K options).

### Visual Identity & Theme
- **Theme**: "Emerald Intelligence Command Center" — enterprise-grade, dark/light adaptive, high data density, minimal distraction.
- **Color Palette**:
  - *Primary*: Deep Emerald (`#0F5132`) & Vivid Mint (`#198754` / `#20C997`) for verification and success states.
  - *Analytical Surfaces*: Slate dark (`#0B0F19`, `#111827`, `#1F2937`) and clean crisp light (`#F8FAFC`, `#FFFFFF`, `#F1F5F9`).
  - *Accents*: Cyan/Teal (`#06B6D4`) for AI processing, Amber (`#F59E0B`) for pending actions, and Coral (`#EF4444`) for disqualified gates.
- **Typography & Hierarchy**: Inter / Roboto Sans for UI telemetry and data grids; Garamond serif preview for ATS CV rendering. Clean hierarchy with distinct labels, status badges, and minimum 48dp touch targets.
- **Interactive Feedback & Motion**: Smooth tab transitions, animated filter chips, expandable gate checks, and animated pulse indicators during Gemini thinking/streaming states.

---

## 3. Key Product Decisions & Trade-Offs

- **Local Persistence with Room**: Uses Android Room database for local caching of jobs, applications, candidate profile, and audit logs. Ensures offline capability, instant responsiveness, and zero cloud lock-in.
- **Direct REST Gemini Client with OkHttp & Moshi**: Implements the official Gemini REST v1beta endpoints directly with 60-second timeouts and full support for `thinkingConfig`, tools (Google Search, Google Maps), image configs, and audio.
- **Factual Source of Truth Guardrails**: Strict rule engine preventing hallucination of candidate metrics; all resume bullet points map strictly to verified candidate history (La Cité, University of Dschang, SIACP, Air Quality API).
- **Single-Activity Jetpack Compose Architecture**: Modular screens with scaffolded navigation bar (Dashboard, Jobs, Applications, ATS Studio, AI Copilot), edge-to-edge drawing, and back handling.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        MainActivity (Compose UI)                       │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
       ┌────────────────────────────┼────────────────────────────┐
       ▼                            ▼                            ▼
┌──────────────┐             ┌──────────────┐             ┌──────────────┐
│  Dashboard   │             │ Job Explorer │             │  ATS Studio  │
│  & Overview  │             │ & Gate Check │             │ & Cover Lett.│
└──────┬───────┘             └──────┬───────┘             └──────┬───────┘
       │                            │                            │
       └────────────────────────────┼────────────────────────────┘
                                    │
                                    ▼
       ┌─────────────────────────────────────────────────────────┐
       │             JobIntelligenceViewModel (MVVM)             │
       └──────────────┬───────────────────────────┬──────────────┘
                      │                           │
                      ▼                           ▼
       ┌─────────────────────────────┐ ┌─────────────────────────┐
       │       Local Repository      │ │     Gemini AI Client    │
       │        (Room Database)      │ │   (REST v1beta / Audio) │
       ├─────────────────────────────┤ ├─────────────────────────┤
       │ - JobEntity / Dao           │ │ - gemini-3.1-pro-preview│
       │ - ApplicationAuditEntity    │ │ - gemini-3.1-flash-lite │
       │ - CandidateProfileDao       │ │ - gemini-3.5-flash      │
       │ - Watchlist & Preferences   │ │ - gemini-3-pro-image    │
       └─────────────────────────────┘ └─────────────────────────┘
```

### Data Models & Entities
1. `JobPost`: ID, title, company, location, salary min/max, remote status (`FULLY_REMOTE`, `HYBRID`, etc.), requirements, match status, source URL.
2. `CandidateProfile`: Michel Dongmo's verified skills (Python, PyTorch, YOLOv8, OpenCV, MLflow, Docker, FastAPI), education (Ph.D ongoing, Collège La Cité, Univ Dschang), verified projects (SIACP, Air Quality ML API).
3. `ApplicationAudit`: Job ID, employer, position, application status (`DISCOVERED`, `QUALIFYING`, `SUBMITTED`, `BLOCKED`), confirmation ID, submission timestamp, ATS CV version.
4. `GeminiChatMessage`: Multi-turn chat message history with role (`user`, `model`), model selection, and thought process logs.

---
