document.addEventListener('DOMContentLoaded', () => {
  const form         = document.getElementById('contactForm');
  const submitBtn    = document.getElementById('submitBtn');
  const loaderCont   = document.getElementById('loaderContainer');  // ← this exists in your HTML
  const status       = document.getElementById('formStatus');

  if (!form || !submitBtn) {
    console.warn("Form or submit button missing – handler not attached.");
    return;
  }

  form.addEventListener('submit', async function(e) {
    e.preventDefault();

    // Loading state
    submitBtn.disabled = true;
    submitBtn.style.opacity = '0.6';                // dim instead of hide (smoother)
    // submitBtn.style.display = 'none';            // ← use this if you prefer full hide

    if (loaderCont) {
      loaderCont.classList.remove('d-none');
    } else {
      console.warn("#loaderContainer not found in DOM");
    }

    if (status) status.textContent = '';

    try {
      const data = {
        name:    form.elements.name?.value.trim()    || '',
        surname: form.elements.surname?.value.trim() || '',
        email:   form.elements.email?.value.trim()   || '',
        message: form.elements.message?.value.trim() || ''
      };

      if (data.message.length < 10) {
        throw new Error('Message is too short – please tell us more.');
      }

      const response = await fetch('http://localhost:8081/api/contact/submit', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });

      if (!response.ok) {
        let errorMsg = `Server error (${response.status})`;
        try {
          const errorBody = await response.json();
          errorMsg = errorBody.message || errorBody.error || errorMsg;
        } catch {}
        throw new Error(errorMsg);
      }

      const result = await response.json();
      if (status) {
        status.textContent = result.message || 'Thank you! Your message has been sent.';
        status.style.color = '#28a745';
      }
      form.reset();

    } catch (err) {
      console.error(err);
      if (status) {
        status.textContent = err.message || 'Sorry, we could not send your message. Please try again.';
        status.style.color = '#dc3545';
      }
    } finally {
      // Cleanup
      submitBtn.disabled = false;
      submitBtn.style.opacity = '1';
      // submitBtn.style.display = 'block';

      if (loaderCont) {
        loaderCont.classList.add('d-none');
      }
    }
  });
});