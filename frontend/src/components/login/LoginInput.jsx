const LoginInput = ({ type, placeholder }) => {
  return (
    <div className="login-input">
      <input type={type} placeholder={placeholder} />
    </div>
  );
};

export default LoginInput;
