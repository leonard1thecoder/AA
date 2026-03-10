// Reset Password handler
document.getElementById('resetPasswordForm').addEventListener('submit', async function(e) {
  e.preventDefault();

  // DOM elements
  const errorDiv      = document.getElementById('errorAlert');
  const successDiv    = document.getElementById('successAlert');
  const newPassInput  = document.getElementById('newPassword');
  const confPassInput = document.getElementById('confirmPassword');
  const submitBtn     = e.target.querySelector('button[type="submit"]');

  // Reset messages
  errorDiv.classList.add('d-none');
  errorDiv.textContent = '';
  successDiv.classList.add('d-none');
  successDiv.textContent = '';

  // Loading state
  submitBtn.disabled = true;
  submitBtn.innerHTML = 'Updating...';

  const newPassword     = newPassInput.value;
  const confirmPassword = confPassInput.value;

  // Basic client-side validation
  if (!newPassword || !confirmPassword) {
    errorDiv.textContent = 'Please fill in both password fields';
    errorDiv.classList.remove('d-none');
    submitBtn.disabled = false;
    submitBtn.innerHTML = 'Update Password';
    return;
  }

  if (newPassword !== confirmPassword) {
    errorDiv.textContent = 'Passwords do not match';
    errorDiv.classList.remove('d-none');
    submitBtn.disabled = false;
    submitBtn.innerHTML = 'Update Password';
    return;
  }

  // Extract reset token from URL (common patterns)
  const urlParams = new URLSearchParams(window.location.search);
  let token = urlParams.get('token');               // ?token=xyz
  if (!token) {
    token = urlParams.get('resetToken');            // ?resetToken=xyz
  }
  if (!token && window.location.pathname.includes('/reset/')) {
    // e.g. /reset/abc123 → take last segment
    token = window.location.pathname.split('/').filter(Boolean).pop();
  }

  if (!token) {
    errorDiv.textContent = 'Reset token is missing. Please use the link from your email.';
    errorDiv.classList.remove('d-none');
    submitBtn.disabled = false;
    submitBtn.innerHTML = 'Update Password';
    return;
  }

  // Prepare payload – adjust field names to match your backend DTO exactly
  const payload = {
    userToken: token,
    usersPassword: newPassword,
    usersConfirmPassword: newPassword,    // ← include only if backend requires it
    // usersPassword: newPassword,      // ← if your naming convention uses "users..."
  };

  try {
    const response = await fetch('http://localhost:8081/dev/api/auth/reset-password', {  // ← CHANGE TO YOUR REAL ENDPOINT
      method: 'POST',   // or PUT — most APIs use POST for reset-with-token
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify(payload)
    });

    if (response.ok) {
      let data = {};
      try {
        data = await response.json();
      } catch {
        // empty 200 OK is also fine
      }

      successDiv.textContent = data.message || 'Password updated successfully! Redirecting to login...';
      successDiv.classList.remove('d-none');

      // Optional: redirect after short delay
      setTimeout(() => {
        window.location.href = '/login';
      }, 2500);
    } 
    else {
      let errorData;
      try {
        errorData = await response.json();
      } catch {
        errorData = { resolveIssueDetails: `Server error (${response.status})` };
      }

      // Flexible error message extraction – match your backend format
      const message = 
        errorData?.resolveIssueDetails ||
        errorData?.message ||
        errorData?.error ||
        errorData?.[0]?.resolveIssueDetails ||   // like your login code
        `Failed to reset password (${response.status})`;

      errorDiv.textContent = message;
      errorDiv.classList.remove('d-none');
    }
  } 
  catch (err) {
    console.error('Reset password error:', err);
    errorDiv.textContent = 'Unable to connect. Please try again or request a new reset link.';
    errorDiv.classList.remove('d-none');
  } 
  finally {
    submitBtn.disabled = false;
    submitBtn.innerHTML = 'Update Password';
  }
});


