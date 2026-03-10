// Forgot Password handler
document.getElementById('forgotPasswordForm').addEventListener('submit', async function(e) {
  e.preventDefault(); // Prevent page reload

  // Get DOM elements
  const errorDiv   = document.getElementById('errorAlert');
  const successDiv = document.getElementById('successAlert');
  const emailInput = document.getElementById('resetEmail');
  const submitBtn  = e.target.querySelector('button[type="submit"]');

  // Reset previous messages
  errorDiv.classList.add('d-none');
  errorDiv.textContent = '';
  successDiv.classList.add('d-none');
  successDiv.textContent = '';

  // Disable button + show loading state
  submitBtn.disabled = true;
  submitBtn.innerHTML = 'Sending...';

  const email = emailInput.value.trim();

  if (!email) {
    errorDiv.textContent = 'Please enter your email address';
    errorDiv.classList.remove('d-none');
    submitBtn.disabled = false;
    submitBtn.innerHTML = 'Send Reset Link';
    return;
  }

  // Prepare payload (adjust field name to match your backend DTO)
  const payload = {
    emailAddress: email   // ← most important: match your backend field name
    // email: email            // ← alternative common name
  };

  try {
    const response = await fetch('http://localhost:8081/dev/api/auth/forgot-password', {  // ← CHANGE TO YOUR REAL ENDPOINT
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify(payload)
    });

    if (response.ok) {  // 200–299
      let data;
      try {
        data = await response.json();
      } catch {
        // some APIs return 200 + empty body on success
        data = {};
      }

      // Customize success message based on your backend response
      successDiv.textContent = data.message 
        || 'If an account exists with this email, you will receive a password reset link shortly.';
      successDiv.classList.remove('d-none');

      // Optional: disable form / hide button after success
      // emailInput.disabled = true;
      // submitBtn.style.display = 'none';
    } 
    else {
      // Error response (400, 404, 500, etc.)
      let errorData;
      try {
        errorData = await response.json();
      } catch {
        errorData = { resolveIssueDetails: `Server error (${response.status})` };
      }

      // Adjust according to your actual error response structure
      const message = errorData?.resolveIssueDetails 
                   || errorData?.message 
                   || errorData?.error 
                   || `Something went wrong (${response.status})`;

      errorDiv.textContent = message;
      errorDiv.classList.remove('d-none');
    }
  } 
  catch (err) {
    // Network error, CORS, timeout, etc.
    console.error('Forgot password error:', err);
    errorDiv.textContent = 'Unable to connect. Please check your internet and try again.';
    errorDiv.classList.remove('d-none');
  }
  finally {
    // Always re-enable button
    submitBtn.disabled = false;
    submitBtn.innerHTML = 'Send Reset Link';
  }
});