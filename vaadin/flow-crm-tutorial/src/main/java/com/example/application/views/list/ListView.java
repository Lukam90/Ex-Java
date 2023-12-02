package com.example.application.views.list;

import java.util.Collections;

import com.example.application.data.Contact;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "")
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout {
    Grid<Contact> grid = new Grid<>(Contact.class);

    TextField filterText = new TextField();
    
    ContactForm form;

    CrmService service;

    public ListView(CrmService service) {
        this.service = service;

        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureForm() {
        form = new ContactForm(Collections.emptyList(), Collections.emptyList());
        form.setWidth("25em");
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(click -> addContact());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());

        updateList();
        closeEditor();
    }

    public void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);

            addClassName("editing");
        }
    }

    public void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());

        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);

        removeClassName("editing");
    }

    private void addContact() {
        grid.asSingleSelect().clear();

        editContact(new Contact());
    }

    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }
}
