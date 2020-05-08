/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2020 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.extension.jwt.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.view.AbstractParamPanel;
import org.zaproxy.zap.extension.jwt.JWTConfiguration;
import org.zaproxy.zap.extension.jwt.JWTI18n;
import org.zaproxy.zap.utils.FontUtils;

/**
 * JWT options panel for specifying settings which are used by {@code JWTActiveScanner} for finding
 * vulnerabilities in applications.
 *
 * @author KSASAN preetkaran20@gmail.com
 * @since TODO add version
 */
public class JWTOptionsPanel extends AbstractParamPanel {
    private static final long serialVersionUID = 1L;

    private String trustStorePath;
    private JScrollPane settingsScrollPane;
    private JPanel footerPanel;
    private JFileChooser trustStoreFileChooser;
    private JPasswordField trustStorePasswordField;
    private String trustStorePassword;
    private JButton trustStoreFileChooserButton;
    private JTextField trustStoreFileChooserTextField;
    private JCheckBox enableClientConfigurationScanCheckBox;

    /** Custom JWT configuration */
    public JWTOptionsPanel() {
        super();
        this.setName(JWTI18n.getMessage("jwt.settings.title"));
        this.setLayout(new BorderLayout());
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsScrollPane =
                new JScrollPane(
                        settingsPanel,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(settingsScrollPane, BorderLayout.NORTH);
        footerPanel = new JPanel();
        this.add(footerPanel, BorderLayout.SOUTH);
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.addFileChooserTextField();
        this.trustStoreFileChooserButton();
        init(settingsPanel);
    }

    private void init(JPanel settingsPanel) {
        settingsPanel.add(this.rsaSettingsSection());
        settingsPanel.add(this.generalSettingsSection());
        footerPanel.add(getResetButton());
    }

    private JButton getResetButton() {
        JButton resetButton = new JButton();
        resetButton.setText(JWTI18n.getMessage("jwt.settings.button.reset"));
        resetButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        resetOptionsPanel();
                    }
                });
        return resetButton;
    }

    private void trustStoreFileChooserButton() {

        trustStoreFileChooserButton =
                new JButton(JWTI18n.getMessage("jwt.settings.filechooser.button"));
        trustStoreFileChooserButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        trustStoreFileChooser = new JFileChooser();
                        trustStoreFileChooser.setFileFilter(
                                new FileFilter() {

                                    @Override
                                    public String getDescription() {
                                        return JWTI18n.getMessage(
                                                "jwt.settings.rsa.trustStoreFileDescription");
                                    }

                                    @Override
                                    public boolean accept(File f) {
                                        return f.getName().endsWith(".p12") || f.isDirectory();
                                    }
                                });
                        trustStoreFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        String path = trustStoreFileChooserTextField.getText();
                        if (!path.isEmpty()) {
                            File file = new File(path);
                            if (file.exists()) {
                                trustStoreFileChooser.setSelectedFile(file);
                            }
                        }
                        if (trustStoreFileChooser.showOpenDialog(null)
                                == JFileChooser.APPROVE_OPTION) {
                            final File selectedFile = trustStoreFileChooser.getSelectedFile();
                            trustStorePath = selectedFile.getAbsolutePath();
                            trustStoreFileChooserTextField.setText(selectedFile.getAbsolutePath());
                        }
                    }
                });
    }

    private void addFileChooserTextField() {
        trustStoreFileChooserTextField = new JTextField();
        trustStoreFileChooserTextField.setEditable(false);
        trustStoreFileChooserTextField.setColumns(15);
    }

    private GridBagConstraints getGridBagConstraints() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weightx = 1.0D;
        gridBagConstraints.weighty = 1.0D;
        return gridBagConstraints;
    }

    private JPanel rsaSettingsSection() {
        JPanel rsaPanel = new JPanel();
        rsaPanel.setSize(rsaPanel.getPreferredSize());
        GridBagLayout gridBagLayout = new GridBagLayout();
        rsaPanel.setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints = this.getGridBagConstraints();
        TitledBorder rsaPanelBorder =
                BorderFactory.createTitledBorder(
                        null,
                        JWTI18n.getMessage("jwt.settings.rsa.header"),
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        FontUtils.getFont(FontUtils.Size.standard));
        rsaPanel.setBorder(rsaPanelBorder);
        JLabel lblTrustStorePathAttribute =
                new JLabel(JWTI18n.getMessage("jwt.settings.rsa.trustStorePath"));
        rsaPanel.add(lblTrustStorePathAttribute, gridBagConstraints);
        gridBagConstraints.gridx++;

        rsaPanel.add(trustStoreFileChooserTextField, gridBagConstraints);
        gridBagConstraints.gridx++;
        rsaPanel.add(trustStoreFileChooserButton, gridBagConstraints);

        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 0;
        JLabel lblTrustStorePassword =
                new JLabel(JWTI18n.getMessage("jwt.settings.rsa.trustStorePassword"));
        rsaPanel.add(lblTrustStorePassword, gridBagConstraints);

        gridBagConstraints.gridx++;
        trustStorePasswordField = new JPasswordField();
        trustStorePasswordField.setColumns(15);
        trustStorePasswordField.addFocusListener(
                new FocusListener() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        if (trustStorePasswordField.getPassword() != null) {
                            trustStorePassword = new String(trustStorePasswordField.getPassword());
                        }
                    }

                    @Override
                    public void focusGained(FocusEvent e) {}
                });
        lblTrustStorePassword.setLabelFor(trustStorePasswordField);
        rsaPanel.add(trustStorePasswordField, gridBagConstraints);
        return rsaPanel;
    }

    private JPanel generalSettingsSection() {
        JPanel generalSettingsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        TitledBorder generalSettingsBorder =
                BorderFactory.createTitledBorder(
                        null,
                        JWTI18n.getMessage("jwt.settings.general.header"),
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        FontUtils.getFont(FontUtils.Size.standard));
        generalSettingsPanel.setBorder(generalSettingsBorder);
        enableClientConfigurationScanCheckBox =
                new JCheckBox(
                        JWTI18n.getMessage("jwt.settings.general.enableClientSideScan.checkBox"));
        generalSettingsPanel.add(enableClientConfigurationScanCheckBox);
        return generalSettingsPanel;
    }

    /** Resets entire panel to default values. */
    private void resetOptionsPanel() {
        trustStorePasswordField.setText("");
        trustStoreFileChooserTextField.setText("");
        trustStorePassword = null;
        enableClientConfigurationScanCheckBox.setSelected(false);
        trustStorePath = "";
    }

    private void populateOptionsPanel() {
        trustStoreFileChooserTextField.setText(trustStorePath);
        trustStorePasswordField.setText(trustStorePassword);
    }

    @Override
    public void initParam(Object optionParams) {
        this.resetOptionsPanel();
        JWTConfiguration jwtConfiguration =
                ((OptionsParam) optionParams).getParamSet(JWTConfiguration.class);
        trustStorePath = jwtConfiguration.getTrustStorePath();
        trustStorePassword = jwtConfiguration.getTrustStorePassword();
        enableClientConfigurationScanCheckBox.setSelected(
                jwtConfiguration.isEnableClientConfigurationScan());
        this.populateOptionsPanel();
    }

    @Override
    public void validateParam(Object optionParams) throws Exception {}

    @Override
    public void saveParam(Object optionParams) throws Exception {
        JWTConfiguration jwtConfiguration =
                ((OptionsParam) optionParams).getParamSet(JWTConfiguration.class);
        jwtConfiguration.setTrustStorePath(trustStorePath);
        jwtConfiguration.setTrustStorePassword(trustStorePassword);
        jwtConfiguration.setEnableClientConfigurationScan(
                enableClientConfigurationScanCheckBox.isSelected());
    }
}
