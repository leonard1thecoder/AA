// Mobile menu toggle
const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
const navLinks = document.querySelector('.nav-links');
const navCta = document.querySelector('.nav-cta');

mobileMenuBtn.addEventListener('click', () => {
   const isOpen = navLinks.classList.toggle('active');
   navCta.classList.toggle('active', isOpen);
   mobileMenuBtn.textContent = isOpen ? '✕' : '☰';

   // Dynamically position CTA below nav-links
   if (isOpen) {
      setTimeout(() => {
         const navLinksHeight = navLinks.offsetHeight;
         navCta.style.top = `calc(100% + ${navLinksHeight}px)`;
      }, 10);
   }
});

// Close mobile menu when clicking a link
navLinks.querySelectorAll('a').forEach(link => {
   link.addEventListener('click', () => {
      navLinks.classList.remove('active');
      navCta.classList.remove('active');
      mobileMenuBtn.textContent = '☰';
   });
});

// Pricing toggle
const PRICING = {
   monthly: {
      price: 30,
      period: '/mo',
      billed: '',
      savings: ''
   },
   quarterly: {
      price: 25.50,
      period: '/mo',
      billed: 'Billed $76.50 every 3 months',
      savings: 'Save 15%'
   },
   yearly: {
      price: 21,
      period: '/mo',
      billed: 'Billed $252 per year',
      savings: 'Save 30%'
   }
};

function updatePricing(billing) {
   const plan = PRICING[billing];

   // Update active button
   document.querySelectorAll('.billing-option').forEach(btn => {
      btn.classList.toggle('active', btn.dataset.billing === billing);
   });

   // Update price display
   const priceEl = document.getElementById('proPrice');
   const billedEl = document.getElementById('proBilled');
   const savingsEl = document.getElementById('proSavings');

   // Animate price change
   priceEl.style.opacity = '0';
   priceEl.style.transform = 'translateY(-10px)';

   setTimeout(() => {
      priceEl.textContent = plan.price % 1 === 0 ? plan.price : plan.price.toFixed(2);
      billedEl.textContent = plan.billed;
      savingsEl.textContent = plan.savings;

      priceEl.style.opacity = '1';
      priceEl.style.transform = 'translateY(0)';
   }, 150);
}

document.querySelectorAll('.billing-option').forEach(btn => {
   btn.addEventListener('click', () => updatePricing(btn.dataset.billing));
});

// Add transition to price element
document.getElementById('proPrice').style.transition = 'all 0.15s ease';

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("contactForm");
  const popup = document.getElementById("popup");
  const okBtn = document.getElementById("okBtn");
  const popupTitle = document.getElementById("popupTitle");
  const popupMessage = document.getElementById("popupMessage");

  form.addEventListener("submit", (event) => {
    event.preventDefault();

    const name = document.getElementById("name");
    const surname = document.getElementById("surname");
    const email = document.getElementById("email");
    const message = document.getElementById("message");

    // Reset borders
    [name, surname, email, message].forEach(field => {
      field.classList.remove("input-error");
    });

    let hasError = false;

    // Check empty fields
    [name, surname, email, message].forEach(field => {
      if (!field.value.trim()) {
        field.classList.add("input-error");
        hasError = true;
      }
    });

    // ✅ Email format validation
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (email.value.trim() && !emailPattern.test(email.value.trim())) {
      email.classList.add("input-error");
      hasError = true;
      popupTitle.textContent = "Invalid email address";
      popupTitle.classList.add("error-message");
      popupMessage.textContent = "Please enter a valid email format (e.g., name@example.com).";
      popup.style.display = "flex";
      return;
    }

    if (hasError) {
      popupTitle.textContent = "Please complete all fields";
      popupTitle.classList.add("error-message");
      popupMessage.textContent = "Highlighted fields are required.";
      popup.style.display = "flex";
      return;
    }

    // Success case
    popupTitle.textContent = "Enquiry successfully submitted";
    popupTitle.classList.remove("error-message");
    popupMessage.textContent = "Thank you for reaching out. We’ll get back to you soon!";
    popup.style.display = "flex";
    form.reset();
  });

  okBtn.addEventListener("click", () => {
    popup.style.display = "none";
  });
});
document.addEventListener('DOMContentLoaded', () => {
  const slides = document.querySelector('.slides');
  const images = document.querySelectorAll('.slides img');
  const prevBtn = document.querySelector('.prev');
  const nextBtn = document.querySelector('.next');

  let index = 0;
  const total = images.length;

  function showSlide(i) {
    index = (i + total) % total;                 // wrap around
    slides.style.transform = `translateX(-${index * 100}%)`;
  }

  // Buttons
  prevBtn.addEventListener('click', () => showSlide(index - 1));
  nextBtn.addEventListener('click', () => showSlide(index + 1));

  // Auto-slide every 6 seconds
  setInterval(() => showSlide(index + 1), 6000);

  // Initialize
  showSlide(0);
});
