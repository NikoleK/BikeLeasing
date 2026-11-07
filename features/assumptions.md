# Key Assumptions & Clarifying Questions (Task 1: Claims Workflow)

## 1. Stolen Bike Recovery Timing & State Machine (Scenarios 3 & 4)

### Assumptions

- Recovery AFTER replacement (Scenario 3): the user keeps the new replacement bike under the active contract. Ownership of the recovered original bike transfers to Bikeleasing / the insurer. The claim state updates to "Recovered".
- Recovery BEFORE replacement (Scenario 4): the claim is immediately canceled. No replacement bike is issued, no insurance payout is made, and the user keeps the original bike.

### Clarifying questions for the PO

- Where is the exact legal "point of no return" for claim cancellation if a replacement bike was ordered but not yet delivered?
- If the recovered bike (Scenario 3) is found damaged, does it automatically trigger a secondary Damage claim evaluation?
- Can the lessee opt to swap back to the recovered original bike if it is found in pristine condition?

## 2. User Notifications & Status Tracking

### Clarifying questions for the PO

- Notifications: is the user notified when a key status changes (routed to warranty, claim canceled, replacement approved, bike recovered)?
- Status tracking: is there a central view in the app where the user can track the live status of the claim across all routes?

## 3. Intake Error Handling

### Clarifying questions for the PO

- Error recovery: what happens if a user selects the wrong bike during submission? Can they edit that choice at the Intake stage without canceling the claim?
