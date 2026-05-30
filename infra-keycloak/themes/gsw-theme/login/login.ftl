<#import "template.ftl" as layout>

<@layout.registrationLayout displayMessage=!messagesPerField.existsError('username','password'); section>

<#if section = "header">
    <div class="kc-header-hidden"></div>

<#elseif section = "form">

<div class="login-page">

    <div class="login-left">

        <div class="login-intro">
            <h1>Seja bem-vindo(a)!</h1>
            <p>Faça login para acessar o sistema</p>
        </div>

        <div class="login-box">

            <#if message?has_content>
                <div class="error-box">
                    ${kcSanitize(message.summary)?no_esc}
                </div>
            </#if>

            <form id="kc-form-login"
                  action="${url.loginAction}"
                  method="post">

                <div class="input-group">
                    <label>Email</label>
                    <input type="text"
                           id="username"
                           name="username"
                           placeholder="email@gsw.com"
                           autofocus />
                </div>

                <div class="input-group">
                    <label>Senha</label>

                    <div class="password-wrapper">
                        <input type="password"
                               id="password"
                               name="password"
                               placeholder="********" />

                        <button type="button"
                                class="eye-btn"
                                onclick="togglePassword()">
                            👁
                        </button>
                    </div>
                </div>

                <button type="submit"
                        id="kc-login"
                        class="btn-login">
                    Entrar
                </button>

            </form>
        </div>

    </div>

    <div class="login-right">
        <img src="${url.resourcesPath}/img/gsw-logo-branco.png"
             alt="GSW Logo" />
    </div>

</div>

<script>
function togglePassword() {
    const input = document.getElementById("password");
    input.type = input.type === "password" ? "text" : "password";
}
</script>

</#if>

</@layout.registrationLayout>