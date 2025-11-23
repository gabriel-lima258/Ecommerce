package com.gtech.Ecommerce.utils;

import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class EmailTemplateUtil {

    public String buildRecoveryEmailHtml(String userName, String recoveryLink, Long validityMinutes) {
        String displayName = userName != null ? userName : "Usuário";
        int currentYear = Year.now().getValue();
        
        return String.format("""
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Recuperação de Senha</title>
            </head>
            <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
                <table role="presentation" style="width: 100%%; border-collapse: collapse;">
                    <tr>
                        <td style="padding: 20px 0; text-align: center; background-color: #ffffff;">
                            <table role="presentation" style="width: 600px; margin: 0 auto; border-collapse: collapse;">
                                <!-- Header -->
                                <tr>
                                    <td style="padding: 30px 20px; text-align: center; background-color: #2c3e50;">
                                        <h1 style="margin: 0; color: #ffffff; font-size: 28px; font-weight: bold;">
                                            Ecommerce
                                        </h1>
                                    </td>
                                </tr>
                                
                                <!-- Content -->
                                <tr>
                                    <td style="padding: 40px 30px; background-color: #ffffff;">
                                        <h2 style="margin: 0 0 20px 0; color: #2c3e50; font-size: 24px;">
                                            Olá, %s!
                                        </h2>
                                        <p style="margin: 0 0 20px 0; color: #555555; font-size: 16px; line-height: 1.6;">
                                            Recebemos uma solicitação para redefinir a senha da sua conta. 
                                            Se você não fez esta solicitação, pode ignorar este email com segurança.
                                        </p>
                                        <p style="margin: 0 0 30px 0; color: #555555; font-size: 16px; line-height: 1.6;">
                                            Para criar uma nova senha, clique no botão abaixo:
                                        </p>
                                        
                                        <!-- Button -->
                                        <table role="presentation" style="width: 100%%; margin: 30px 0;">
                                            <tr>
                                                <td style="text-align: center;">
                                                    <a href="%s" 
                                                       style="display: inline-block; padding: 15px 40px; background-color: #3498db; 
                                                              color: #ffffff; text-decoration: none; border-radius: 5px; 
                                                              font-size: 16px; font-weight: bold;">
                                                        Redefinir Senha
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <p style="margin: 30px 0 10px 0; color: #888888; font-size: 14px; line-height: 1.6;">
                                            Ou copie e cole o link abaixo no seu navegador:
                                        </p>
                                        <p style="margin: 0 0 30px 0; color: #3498db; font-size: 14px; word-break: break-all;">
                                            %s
                                        </p>
                                        
                                        <p style="margin: 0; color: #e74c3c; font-size: 14px; font-weight: bold;">
                                            ⏰ Este link expira em %d minutos.
                                        </p>
                                    </td>
                                </tr>
                                
                                <!-- Footer -->
                                <tr>
                                    <td style="padding: 30px 20px; text-align: center; background-color: #ecf0f1; border-top: 3px solid #3498db;">
                                        <p style="margin: 0 0 10px 0; color: #7f8c8d; font-size: 14px;">
                                            Este é um email automático, por favor não responda.
                                        </p>
                                        <p style="margin: 0; color: #7f8c8d; font-size: 12px;">
                                            © %d Ecommerce. Todos os direitos reservados.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """, displayName, recoveryLink, recoveryLink, validityMinutes, currentYear);
    }
}









