# DIN Test – hearX Technical Assessment

## Overview
This project implements a **DIN** hearing test as part of the hearX Android Developer technical assessment.

The application focuses on:
- Adaptive test logic across multiple rounds
- Audio sequencing of noise and digit playback
- Uploading test results to a backend API
- Persisting completed test results locally for later review

---

## App Flow
1. **Home Screen**
    - Start Test
    - View Results (previously completed tests)

2. **Test Screen**
    - 10 rounds
    - Each round plays a triplet of digits over background noise
    - Difficulty adapts based on correctness
    - User submits the perceived digits

3. **Completion**
    - Test results are uploaded to the backend
    - Final score is presented after a successful upload
    - Results are saved locally and can be viewed later

---

## Architecture & Design Decisions
The application follows an MVVM-style architecture:

- **UI Layer**
    - Built with Jetpack Compose
    - Renders state exposed by ViewModels only

- **ViewModels**
    - Own UI state and timing
    - Coordinate test flow, audio playback, and submission

- **Domain / Session Layer**
    - Encapsulates test rules:
        - Round progression
        - Difficulty adjustment
        - Scoring
        - Triplet generation and constraints

- **Audio Layer**
    - Dedicated audio player component
    - Responsible for sequencing background noise and digit playback

- **Networking**
    - Implemented using **Retrofit**
    - Responsible for uploading completed test results to the backend API

- **Persistence**
    - Implemented using **Room**
    - Completed test results are stored locally after a successful upload
    - Results are displayed in descending score order

---

## Key Assumptions
- The test consists of exactly **10 rounds**.
- Test difficulty starts at **5** and is clamped between **1 and 10**.
- **Scoring interpretation:**  
  Each round awards points equal to the difficulty (noise file) used for that round.  
  Correctness affects *subsequent difficulty*, not whether points are awarded.
- Test results are saved locally **only after a successful upload**.
- Short audio delays were chosen to balance clarity and simplicity.
- Network calls are assumed to be reliable for this pre-commercial exercise; upload failures are surfaced to the user with a retry option.

