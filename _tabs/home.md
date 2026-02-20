---
layout: page
permalink: /
icon: fas fa-home
order: 1
---

<style>
/* Hide the automatically generated H1 title */
.dynamic-title {
  display: none;
}

/* Hide the search bar completely */
#search-trigger,
#search,
#search-cancel,
#search-result-wrapper {
    display: none !important;
}

/* Hide the breadcrumb home title */
#breadcrumb {
  display: none;
}

/* Sidebar links */
/* Base style */
.nav-link {
  color: #333;
  font-weight: normal;
  text-decoration: none;
  transition: color 0.2s, font-weight 0.2s;
}

/* Highlight only on hover */
.nav-link:hover {
  color: #007bff;       /* blue highlight */
  font-weight: bold;
}

/* Remove active/visited styling after click */
.nav-link:visited,
.nav-link:focus,
.nav-link:active {
  color: #333;           /* back to normal */
  font-weight: normal;
}

	/* Smooth color transition for Firefox */
	.nav-link:focus-visible {
		outline: none;         /* remove default focus outline */
	}
}
</style>

# Welcome to My ePortfolio!

---

## Professional Self-Assessment {#self-assessment}

<p style="text-indent: 2em;">
	My name is Arielle, and I am concluding my Bachelor of Science in Computer Science as I build upon over four years of experience as a fullstack application software developer. While it's true that I've already had professional experience before embarking on this degree, I was still eager to deepen my theoretical foundation and to formalize the core principles that define strong software engineering. What I didn't anticipate was the extent to which the program would sharpen not only my technical skills, but also my discipline, judgement, and overall approach as an engineer.
</p>

<p style="text-indent: 2em;">
	Collaboration has been integral to my development throughout both my academic and professional careers. In the software industry, I work closely with product managers, QA analysts, designers, and fellow developers to translate stakeholder requirements into systems that are both reliable and scalable. This program has sharpened my ability to articulate technical decisions with clarity and precision, whether through formal documentation, code reviews, testing rationale, or architectural discussion. It has also underscored the necessity of designing systems that are not merely functional, but maintainable and extensible over time. Concepts such as data structures, algorithmic efficiency, relational database design, and secure development practices were not simply abstract academic exercises; in concert, they directly impacted the way I evaluate trade-offs within real-world systems. This focus also ensures my ability to design systems resilient to common vulnerabilities and compliant with organizational security policies while encouraging responsible practices within my development team.
</p>

<p style="text-indent: 2em;">
	One of the more gratifying aspects of completing this capstone has been the opportunity to revisit my earlier work and observe, with some measure of detachment, the extent to which my thinking has evolved. Projects that once appeared complete now present clear opportunities for improved separation of concerns, more disciplined abstractions, and a more deliberate architectural approach. That recognition is not disheartening; rather, it is instructive. Growth in computer science is seldom about dramatic reinvention and is more often the result of careful refinement. The ability to identify technical debt, anticipate scalability constraints, and design proactively for maintainability marks a transition from just producing code to engineering coherent, durable systems. Through this reflection, I recognized that even small efficiency improvements or design refinements can yield measurable benefits, such as reduced system load and improved responsiveness.
</p>

<p style="text-indent: 2em;">
	The artifacts within this portfolio trace that progression. The first enhancement demonstrates architectural refactoring, separating tightly-coupled Android Activity logic into dedicated service classes, and emphasizing modularity and clarity overall. The second enhancement demonstrates algorithmic and data structure improvements, replacing primitive arrays with structured object collections, and implementing efficient sorting and filtering operations that respond directly to user interaction. The third enhancement concentrates on database integrity and scalability, incorporating foreign key constraints, cascading deletes, transactional safeguards, optimized queries, and verifying with unit testing. In unison, these artifacts represent more than completed assignments -- they optimistically illustrate a cohesive understanding of how application layers interact, and the necessity of validating improvements across the system as a whole. They also showcase my ability to integrate theoretical concepts from coursework into enterprise-grade solutions.
</p>

<p style="text-indent: 2em;">
	This program has reinforced my persistent professional values: prioritizing thoughtful design over quick fixes, treating testing as an essential safeguard rather than a postscript, and recognizing security and data integrity as fundamental responsibilities. It has also bolstered my confidence in translating theoretical knowledge into enterprise practice.
</p>

<p style="text-indent: 2em;">
	I regard graduation not as a conclusion, but as a formal recognition of competencies that I will continue to cultivate throughout my career. As I move forward, I intend to construct scalable and responsible systems while contributing meaningfully to the teams and organizations I serve. This portfolio reflects not only what I have built, but also the reasoning that underpins it, and the trajectory along which I will continue to develop as a computer science professional.
</p>

Thank you for stopping by! Feel free to reach out with any questions or feedback.

---

## Code Review {#code-review}

<div class="artifact-card">
  <p>This is a demonstration of an informal code review process that highlights potential enhancements prior to the implementation stage.</p>

  <div style="position: relative; padding-bottom: 56.25%; height: 0;">
    <iframe
      src="https://drive.google.com/file/d/1YoPoGk8B1ZTV-GxipBgfVZoQIECFfUIq/preview"
      style="position: absolute; top:0; left:0; width:100%; height:100%;"
      allow="autoplay"
      allowfullscreen>
    </iframe>
	</div>

  <p><em>Click play to watch the video.</em></p>
</div>

---

## Code Artifact Enhancements {#artifact-enhancements}

<div class="artifact-cards" style="display:flex; flex-direction:column; gap:1.5rem; width:100%;">

  <!-- Enhancement One -->
  <div class="artifact-card" style="border:1px solid #ddd; border-radius:8px; padding:1rem; width:100%;">
    <h4>Enhancement One: Software Design and Engineering</h4>
    <p>Implemented robust software design principles to improve maintainability and scalability.</p>
    <a href="https://github.com/aricmoore/Enhancement-One-Design" target="_blank" rel="noopener noreferrer">View Repository →</a>
  </div>

  <!-- Enhancement Two -->
  <div class="artifact-card" style="border:1px solid #ddd; border-radius:8px; padding:1rem; width:100%;">
    <h4>Enhancement Two: Algorithms and Data Structures</h4>
    <p>Optimized algorithms and data structures for efficient computation and performance.</p>
    <a href="https://github.com/aricmoore/Enhancement-Two-Design" target="_blank" rel="noopener noreferrer">View Repository →</a>
  </div>

  <!-- Enhancement Three -->
  <div class="artifact-card" style="border:1px solid #ddd; border-radius:8px; padding:1rem; width:100%;">
    <h4>Enhancement Three: Databases</h4>
    <p>Designed and managed database schemas to ensure data integrity and accessibility.</p>
    <a href="https://github.com/aricmoore/Enhancement-Three-Design" target="_blank" rel="noopener noreferrer">View Repository →</a>
  </div>

</div>

<script>
document.addEventListener("DOMContentLoaded", () => {
  const navItems = document.querySelectorAll('.nav-item');
  const links = document.querySelectorAll('.nav-item a');
  const sections = Array.from(document.querySelectorAll('h2[id], h3[id]'));
  
  function updateActive() {
    const scrollPos = window.scrollY + window.innerHeight / 3;

    let activeId = "top"; // default Home
    for (let sec of sections) {
      if (sec.offsetTop <= scrollPos) {
        activeId = sec.id;
      }
    }

    navItems.forEach(li => li.classList.remove('active'));
    const activeLink = document.querySelector(`.nav-link[href="#${activeId}"]`);
    if (activeLink) activeLink.parentElement.classList.add('active');
  }

  window.addEventListener('scroll', updateActive);
  updateActive(); // run on page load
});
</script>