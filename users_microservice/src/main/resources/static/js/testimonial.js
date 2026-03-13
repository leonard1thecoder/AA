// Fix Bootstrap carousel fade + dots generation
document.addEventListener('DOMContentLoaded', function () {
  const carousel = document.querySelector('#testimonialsCarousel');
  if (!carousel) return;

  const carouselInstance = new bootstrap.Carousel(carousel, {
    interval: 7000,
    ride: true,
    pause: 'hover',
    touch: true
  });

  // Generate indicators dynamically
  const indicators = carousel.querySelector('.carousel-indicators');
  const items = carousel.querySelectorAll('.carousel-item');
  
  items.forEach((item, index) => {
    const button = document.createElement('button');
    button.type = 'button';
    button.dataset.bsTarget = '#testimonialsCarousel';
    button.dataset.bsSlideTo = index;
    button.setAttribute('aria-label', `Slide ${index + 1}`);
    
    if (index === 0) {
      button.classList.add('active');
      button.setAttribute('aria-current', 'true');
    }
    
    indicators.appendChild(button);
  });

  // Force first slide to be visible (fixes disappear bug)
  setTimeout(() => {
    carousel.querySelector('.carousel-item.active').style.opacity = '1';
  }, 100);
});
