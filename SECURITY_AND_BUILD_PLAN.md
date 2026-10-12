# FARIS implementation checklist

## Phase 1 — UI shell
- [x] Compose starter screen
- [x] Holographic orb visual and expand/collapse
- [x] Listening/thinking/speaking visual states (manual prototype controls)
- [ ] Test on Realme P4x 5G

## Phase 2 — Core assistant
- [ ] Speech-to-text and text-to-speech provider selection
- [ ] Interrupt speech immediately when the user starts speaking
- [ ] AI backend integration and web search
- [ ] Language matching for Hindi/Hinglish/English
- [ ] Error handling, offline states, timeouts, retry limits

## Phase 3 — Owner verification and privacy
- [ ] Explicit owner enrollment flow with a fixed phrase
- [ ] Store voice template securely; document retention and deletion
- [ ] Liveness/anti-spoof checks; never treat voice match as perfect
- [ ] BiometricPrompt + fallback PIN
- [ ] Encrypt local secrets/data with Android Keystore-backed keys
- [ ] Mic state indicator, manual kill switch, opt-in background listening
- [ ] Never execute sensitive actions from an unauthenticated voice match alone

## Phase 4 — Android integrations
- [ ] Overlay permission flow with clear explanation
- [ ] Foreground microphone service and persistent notification where required
- [ ] Test Android background-start and lock-screen restrictions
- [ ] App launching, reminders, and only supported phone actions
- [ ] No bypass of Android security restrictions

## Phase 5 — QA/release
- [ ] Build debug APK
- [ ] Test crashes, lifecycle, permissions, screen rotation, low-memory conditions
- [ ] Test battery impact and audio interruption
- [ ] Test voice spoofing and false rejection/acceptance
- [ ] Review data flow and provider privacy policies
- [ ] Build release APK only after tests pass
