# university-management
A fullstack application for managing students, teachers and courses

## Steps to run
* git clone https://github.com/Sauhardstark/university-management.git
* cd university-management
* docker compose up --build

### Backend API: http://localhost:8080/api/...

### Frontend:  http://localhost:8081/


## Tradeoffs

### Handling race conditions during enrollment creation and checking course overlaps
**Options:**
- **Check-then-set:** Keep logic simple; rely on DB to reject duplicates.
- **Pessimistic locks (`FOR UPDATE`):** Block conflicting writers by locking the student at the cost of throughput.

**Chosen:** *Check-then-set* — lowest complexity for a non-prod app at current scale.

---

### Entities vs. response wrappers
**Options:**
- **Return JPA entities directly:** Fast to start, but leaks persistence and hits lazy-load issues.
- **DTOs in `ResponseEntity`:** Stable API contract, explicit status/headers, avoids lazy pitfalls.
- **Entities with `@JsonIgnore`/views:** Reduces exposure but remains brittle to entity changes.

**Chosen:** *DTOs in `ResponseEntity`* — clean API boundaries and no lazy-loading surprises.

---

### Fetching complicated multiple tables data
**Options:**
- **Standard option:** Just fetch the required entities and then wait for lazy loading to fetch the rest. Can lead to N+1 queries issue.
- **Spring Data projections (interface/DTO):** Select only needed columns.
- **Custom JPQL/Native DTO queries:** Fine-tuned selects with explicit mapping.
- **Fetch joins + pagination-aware design:** Load related data in one go when needed.

**Chosen:** *Spring Data projections* — simplest way to reduce payload size.

---

### Audit/logging (app vs. DB)
**Options (limited):**
- **Application-level audit table:** Full request/user context; easy to test/version.
- **DB triggers:** Guaranteed capture on any write path but limited context and harder to evolve.
- **CDC stream:** Rich history/replication at higher operational cost.

**Chosen:** *Application-level audit table* — control and context fit our app-specific needs.

---

### Container images (single vs. multiple)
**Options (limited):**
- **Single image (all-in-one):** Simplest deploy, as there is only a single image; tightly coupled lifecycle, use supervisord etc.
- **Separate images (frontend/backend):** Independent release/scale, ease of testing and debugging

**Chosen:** *Separate images* — fastest path to run and deploy while the project is small

### Modeling the Course–Student relationship
**Options:**
- **Per-course tables:** Simple to reason about per course, but causes schema sprawl and difficulties with scaling.
- **Single join table (RDBMS):** Normalized `enrollments(student_id, course_id, …)` with FK/unique constraints; easy to add fields like grade/created_at.
- **Graph DB:** Natural `(Student)-[ENROLLED_IN]->(Course)` modeling and path queries; - crosses infra bounds.
- **Document DB (embedded sets):** Fast reads on one side (e.g., student → courses), but weak constraints and duplication.

**Chosen:** *Single join table* — best fit for relational stack, strong constraints, and easy to change with extra attributes.


### API shape for enrolling a student
**Options:**
- **Nested URIs (both sides):**  
  - `/students/{sid}/enrollments/{cid}` and `/courses/{cid}/enrollments/{sid}`  
  - Intuitive logic; emphasizes the fundamental reliance of enrollment on corse and students but duolicates logic in two paths.
- **Canonical resource:**  
  - `POST /enrollments` with `{student_id, course_id}`; `DELETE /enrollments/{id}` or by composite key  
  - Single code path, clean validation, but makes it a top level resource which can exist independently.

**Chosen:** *Nested URIs* — Show clear dimension on which are the primary resources and which are the dependent ones.


### Search trigger behavior
**Options:**
- **Button click:** Direct call, minimal API calls; slower feedback.
- **onChange with debounce (300–500ms):** Better UX; more backend traffic, needs cancelation and rate limiting.
- **Thresholded query:** Cuts noise; still fires often during edits; arbitrary thresholds.

**Chosen:** *Button click* — it reduces unnecessary backend calls and keeps requests intentional at current scale.


## Limitations
* Concurrency issues can cause overlapping courses
* No authentication or authorization so everyone is free to make changes
* Haven't paginated the results so if the data grows, the pages would start to have latency problems
* Since logs are at application level, other apps or direct interventions could cause misses in database operations that aren't logged
* Haven't added created_at and updated_at so difficult to know when was a particular entity touched last
* Database is ephemeral so no storage across sessions

## Schema Design
