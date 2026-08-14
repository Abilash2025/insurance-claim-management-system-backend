package com.icms.notificationservice.util;

import com.icms.notificationservice.event.claim.ClaimApprovedEvent;
import com.icms.notificationservice.event.claim.ClaimCreatedEvent;
import com.icms.notificationservice.event.claim.ClaimRejectedEvent;
import com.icms.notificationservice.event.customer.CustomerActivatedEvent;
import com.icms.notificationservice.event.customer.CustomerCreatedEvent;
import com.icms.notificationservice.event.customer.CustomerDeactivatedEvent;
import com.icms.notificationservice.event.policy.PolicyActivatedEvent;
import com.icms.notificationservice.event.policy.PolicyCreatedEvent;
import com.icms.notificationservice.event.policy.PolicyDeactivatedEvent;
import com.icms.notificationservice.event.policy.PolicyUpdatedEvent;
import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {

    private EmailTemplateBuilder(){}

    // ==========================================================
    // CUSTOMER
    // ==========================================================

    public String customerCreated(CustomerCreatedEvent event){

        return buildTemplate(
                "Customer Account Created",
                """
                <tr><td><b>Customer ID</b></td><td>%d</td></tr>
                <tr><td><b>Status</b></td><td>%s</td></tr>
                """.formatted(
                        event.getCustomerId(),
                        event.getStatus()
                )
        );
    }

    public String customerActivated(CustomerActivatedEvent event){

        return buildTemplate(
                "Customer Activated",
                """
                <tr><td><b>Customer ID</b></td><td>%d</td></tr>
                <tr><td><b>Status</b></td><td>%s</td></tr>
                """.formatted(
                        event.getCustomerId(),
                        event.getStatus()
                )
        );
    }

    public String customerDeactivated(CustomerDeactivatedEvent event){

        return buildTemplate(
                "Customer Deactivated",
                """
                <tr><td><b>Customer ID</b></td><td>%d</td></tr>
                <tr><td><b>Status</b></td><td>%s</td></tr>
                """.formatted(
                        event.getCustomerId(),
                        event.getStatus()
                )
        );
    }

    // ==========================================================
    // POLICY
    // ==========================================================

    public String policyCreated(PolicyCreatedEvent event){

        return buildTemplate(
                "Policy Created",
                """
                <tr><td><b>Policy Number</b></td><td>%d</td></tr>
                <tr><td><b>Customer ID</b></td><td>%d</td></tr>
                <tr><td><b>Policy Type</b></td><td>%s</td></tr>
                <tr><td><b>Coverage</b></td><td>₹%.2f</td></tr>
                <tr><td><b>Status</b></td><td>%s</td></tr>
                """.formatted(
                        event.getPolicyNumber(),
                        event.getCustomerId(),
                        event.getPolicyType(),
                        event.getCoverage(),
                        event.getStatus()
                )
        );
    }

    public String policyUpdated(PolicyUpdatedEvent event){

        return buildTemplate(
                "Policy Updated",
                """
                <tr><td><b>Policy Number</b></td><td>%d</td></tr>
                <tr><td><b>Policy Type</b></td><td>%s</td></tr>
                <tr><td><b>Premium</b></td><td>₹%.2f</td></tr>
                <tr><td><b>Coverage</b></td><td>₹%.2f</td></tr>
                """.formatted(
                        event.getPolicyNumber(),
                        event.getPolicyType(),
                        event.getPremium(),
                        event.getCoverage()
                )
        );
    }

    public String policyActivated(PolicyActivatedEvent event){

        return buildTemplate(
                "Policy Activated",
                """
                <tr><td><b>Policy Number</b></td><td>%d</td></tr>
                <tr><td><b>Status</b></td><td>%s</td></tr>
                """.formatted(
                        event.getPolicyNumber(),
                        event.getStatus()
                )
        );
    }

    public String policyDeactivated(PolicyDeactivatedEvent event){

        return buildTemplate(
                "Policy Deactivated",
                """
                <tr><td><b>Policy Number</b></td><td>%d</td></tr>
                <tr><td><b>Status</b></td><td>%s</td></tr>
                """.formatted(
                        event.getPolicyNumber(),
                        event.getStatus()
                )
        );
    }

    // ==========================================================
    // CLAIM
    // ==========================================================

    public String claimCreated(ClaimCreatedEvent event){

        return buildTemplate(
                "Claim Submitted",
                """
                <tr><td><b>Claim Number</b></td><td>%d</td></tr>
                <tr><td><b>Policy Number</b></td><td>%d</td></tr>
                <tr><td><b>Claim Amount</b></td><td>₹%.2f</td></tr>
                <tr><td><b>Claim Date</b></td><td>%s</td></tr>
                <tr><td><b>Status</b></td><td>%s</td></tr>
                """.formatted(
                        event.getClaimNumber(),
                        event.getPolicyNumber(),
                        event.getClaimAmount(),
                        event.getClaimDate(),
                        event.getStatus()
                )
        );
    }

    public String claimApproved(ClaimApprovedEvent event){

        return buildTemplate(
                "Claim Approved",
                """
                <tr><td><b>Claim Number</b></td><td>%d</td></tr>
                <tr><td><b>Status</b></td><td>%s</td></tr>
                """.formatted(
                        event.getClaimNumber(),
                        event.getStatus()
                )
        );
    }

    public String claimRejected(ClaimRejectedEvent event){

        return buildTemplate(
                "Claim Rejected",
                """
                <tr><td><b>Claim Number</b></td><td>%d</td></tr>
                <tr><td><b>Status</b></td><td>%s</td></tr>
                """.formatted(
                        event.getClaimNumber(),
                        event.getStatus()
                )
        );
    }

    // ==========================================================
    // COMMON TEMPLATE
    // ==========================================================

    private String buildTemplate(String title, String rows){

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
        </head>

        <body style="margin:0;padding:30px;background:#f3f6fa;
                     font-family:Arial,Helvetica,sans-serif;">

            <table align="center"
                   width="650"
                   cellpadding="0"
                   cellspacing="0"
                   style="background:white;
                          border-radius:10px;
                          box-shadow:0 2px 8px rgba(0,0,0,.15);">

                <tr>
                    <td style="
                        background:#1565c0;
                        color:white;
                        text-align:center;
                        padding:20px;
                        font-size:24px;
                        font-weight:bold;">
                        ICMS Notification
                    </td>
                </tr>

                <tr>
                    <td style="padding:30px;">

                        <h2 style="color:#1565c0;">
                            %s
                        </h2>

                        <p>
                            Dear Customer,
                        </p>

                        <p>
                            Please find your latest insurance update below.
                        </p>

                        <table width="100%%"
                               cellpadding="10"
                               cellspacing="0"
                               style="border-collapse:collapse;
                                      border:1px solid #ddd;">

                            %s

                        </table>

                        <br>

                        <p>
                            Thank you for choosing
                            <b>ICMS Insurance</b>.
                        </p>

                        <p>
                            Regards,<br>
                            ICMS Notification Service
                        </p>

                    </td>
                </tr>

                <tr>
                    <td style="
                        background:#eeeeee;
                        text-align:center;
                        padding:15px;
                        color:#777;
                        font-size:12px;">
                        © 2026 ICMS Insurance Management System
                    </td>
                </tr>

            </table>

        </body>
        </html>
        """.formatted(title, rows);
    }
}
