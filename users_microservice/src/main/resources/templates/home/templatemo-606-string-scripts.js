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