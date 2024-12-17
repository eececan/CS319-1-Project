package com.project.btoproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.btoproject.dto.UserAdvisorDto;
import com.project.btoproject.dto.UserCoordinatorDto;
import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.dto.UserGuideInTrainingDto;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IAllUsersRepository;
import com.project.btoproject.repository.IUserTaskRepository;
import com.project.btoproject.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AllUsersService implements IAllUsersService {
    private final IAllUsersRepository repository;
    private final IUserTaskRepository userTaskRepository;
    private final IGuideService guideService;
    private final IAdvisorService advisorService;
    private final IGuideInTrainingService guideInTrainingService;
    private final UserRepository userRepository;
    private final CoordinatorService coordinatorService;

    public AllUsersService(IAllUsersRepository repository, IUserTaskRepository userTaskRepository, IGuideService guideService, IAdvisorService advisorService, IGuideInTrainingService guideInTrainingService, UserRepository userRepository, CoordinatorService coordinatorService) {
        this.repository = repository;
        this.userTaskRepository = userTaskRepository;
        this.guideService = guideService;
        this.advisorService = advisorService;
        this.guideInTrainingService = guideInTrainingService;
        this.userRepository = userRepository;
        this.coordinatorService = coordinatorService;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void addUser(User user) {
       repository.save(user);
    }

    @Override
    public User getUserById(Long userId) { return repository.findById(userId).get(); }

    @Override
    public List<UserTask> seeAllTasks(User user) {
        return repository.findUserTasksById(user.getId());
    }

    @Override
    public void addTaskToUser(User user, UserTask newTask) {
        newTask.setUser(user);
        userTaskRepository.save(newTask);
        user.getTasks().add(newTask);
        repository.save(user);
    }

    @Override
    public boolean updateTaskStatus(User user, Long taskId, boolean b) {
        List<UserTask> tasks = user.getTasks();
        UserTask taskToUpdate = tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .orElse(null);
        if (taskToUpdate == null) {
            return false;
        }
        taskToUpdate.setState(b);
        userTaskRepository.save(taskToUpdate);
        return true;
    }

    //@Override
    public boolean deleteTaskFromUser(User user, Long taskId) {
        List<UserTask> tasks = user.getTasks();
        UserTask taskToRemove = tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .orElse(null);
        if (taskToRemove == null) {
            return false;
        }
        tasks.remove(taskToRemove);
        userTaskRepository.delete(taskToRemove);
        repository.save(user);
        return true;
    }

    @Override
    public void changeRole(User user, Role role) {

    }

    @Override
    public boolean hasMissingInformation(User user, UserEntity userEntity) {
        boolean missing = false;
        if(userEntity.getRoles().contains("ROLE_GUIDE")){
            Guide guide = guideService.getGuideById(user.getId());
            if(guide.getSchedule().isEmpty()){
                missing = true;
                return missing;
            }
            if(guide.getDepartment().isEmpty()){
                missing = true;
                return missing;
            }
            if(guide.getGrade() == null){
                missing = true;
                return missing;
            }
            if(guide.getFirstName().isEmpty()){
                missing = true;
                return missing;
            }
            if(guide.getLastName().isEmpty()){
                missing = true;
                return missing;
            }
            if(guide.getPhoneNumber().isEmpty()){
                missing = true;
                return missing;
            }
            if(guide.getEmail().isEmpty()){
                missing = true;
                return missing;
            }
            if(guide.getPicture().isEmpty()){
                missing = true;
                return missing;
            }
            if(guide.getDescription().isEmpty()){
                missing = true;
                return missing;
            }
        }
        else if(userEntity.getRoles().contains("ROLE_ADVISOR")){
            Advisor advisor = advisorService.getAdvisorById(user.getId());
            if(advisor.getDepartment().isEmpty()){
                missing = true;
                return missing;
            }
            if(advisor.getGrade() == null){
                missing = true;
                return missing;
            }
            if(advisor.getFirstName().isEmpty()){
                missing = true;
                return missing;
            }
            if(advisor.getLastName().isEmpty()){
                missing = true;
                return missing;
            }
            if(advisor.getPhoneNumber().isEmpty()){
                missing = true;
                return missing;
            }
            if(advisor.getEmail().isEmpty()){
                missing = true;
                return missing;
            }
            if(advisor.getPicture().isEmpty()){
                missing = true;
                return missing;
            }
            if(advisor.getDescription().isEmpty()){
                missing = true;
                return missing;
            }
        }
        else if(userEntity.getRoles().contains("ROLE_GUIDE_IN_TRAINING")){
            GuideInTraining guideInTraining = guideInTrainingService.getGuideInTrainingById(user.getId());
            if(guideInTraining.getSchedule().isEmpty()){
                missing = true;
                return missing;
            }
            if(guideInTraining.getDepartment().isEmpty()){
                missing = true;
                return missing;
            }
            if(guideInTraining.getGrade() == null){
                missing = true;
                return missing;
            }
            if(guideInTraining.getFirstName().isEmpty()){
                missing = true;
                return missing;
            }
            if(guideInTraining.getLastName().isEmpty()){
                missing = true;
                return missing;
            }
            if(guideInTraining.getPhoneNumber().isEmpty()){
                missing = true;
                return missing;
            }
            if(guideInTraining.getEmail().isEmpty()){
                missing = true;
                return missing;
            }
            if(guideInTraining.getPicture().isEmpty()){
                missing = true;
                return missing;
            }
            if(guideInTraining.getDescription().isEmpty()){
                missing = true;
                return missing;
            }
        }
        return missing;
    }

    @Override
    public boolean hasUserWithId(Long id) {
        Optional<User> user = repository.findById(id);
        return user.isPresent();
    }

    @Override
    public void updateProfile(Long id, Map<String, Object> dtoMap) {
        UserEntity userEntity = userRepository.findByUsername(id.toString()).get();
        ObjectMapper objectMapper = new ObjectMapper();
        if (userEntity.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_GUIDE"))) {
            UserGuideDto userGuideDto = objectMapper.convertValue(dtoMap, UserGuideDto.class);
            Guide user = guideService.getGuideById(id);
            if (user != null) {
                if (userGuideDto.getSchedule() != null) user.setSchedule(userGuideDto.getSchedule());
                if (userGuideDto.getDepartment() != null) user.setDepartment(userGuideDto.getDepartment());
                if (userGuideDto.getGrade() != null) user.setGrade(userGuideDto.getGrade());
                if (userGuideDto.getFirstName() != null) user.setFirstName(userGuideDto.getFirstName());
                if (userGuideDto.getLastName() != null) user.setLastName(userGuideDto.getLastName());
                if (userGuideDto.getPhoneNumber() != null) user.setPhoneNumber(userGuideDto.getPhoneNumber());
                if (userGuideDto.getEmail() != null) user.setEmail(userGuideDto.getEmail());
                if (userGuideDto.getPicture() != null) user.setPicture(userGuideDto.getPicture());
                if (userGuideDto.getDescription() != null) user.setDescription(userGuideDto.getDescription());

                repository.save(user);
            } else {
                System.out.println("User not found");
            }
        } else if (userEntity.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADVISOR"))) {
            UserAdvisorDto userAdvisorDto = objectMapper.convertValue(dtoMap, UserAdvisorDto.class);
            Advisor user = advisorService.getAdvisorById(id);
            if (user != null) {
                if (userAdvisorDto.getDepartment() != null) user.setDepartment(userAdvisorDto.getDepartment());
                if (userAdvisorDto.getGrade() != null) user.setGrade(userAdvisorDto.getGrade());
                if (userAdvisorDto.getFirstName() != null) user.setFirstName(userAdvisorDto.getFirstName());
                if (userAdvisorDto.getLastName() != null) user.setLastName(userAdvisorDto.getLastName());
                if (userAdvisorDto.getPhoneNumber() != null) user.setPhoneNumber(userAdvisorDto.getPhoneNumber());
                if (userAdvisorDto.getEmail() != null) user.setEmail(userAdvisorDto.getEmail());
                if (userAdvisorDto.getPicture() != null) user.setPicture(userAdvisorDto.getPicture());
                if (userAdvisorDto.getDescription() != null) user.setDescription(userAdvisorDto.getDescription());
                repository.save(user);
            } else {
                System.out.println("User not found");
            }

        } else if (userEntity.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_GUIDE_IN_TRAINING"))) {
            UserGuideInTrainingDto userGuideInTrainingDto = objectMapper.convertValue(dtoMap, UserGuideInTrainingDto.class);
            GuideInTraining user = guideInTrainingService.getGuideInTrainingById(id);
            if (user != null) {
                if (userGuideInTrainingDto.getDepartment() != null) user.setDepartment(userGuideInTrainingDto.getDepartment());
                if (userGuideInTrainingDto.getGrade() != null) user.setGrade(userGuideInTrainingDto.getGrade());
                if (userGuideInTrainingDto.getFirstName() != null) user.setFirstName(userGuideInTrainingDto.getFirstName());
                if (userGuideInTrainingDto.getLastName() != null) user.setLastName(userGuideInTrainingDto.getLastName());
                if (userGuideInTrainingDto.getPhoneNumber() != null) user.setPhoneNumber(userGuideInTrainingDto.getPhoneNumber());
                if (userGuideInTrainingDto.getEmail() != null) user.setEmail(userGuideInTrainingDto.getEmail());
                if (userGuideInTrainingDto.getPicture() != null) user.setPicture(userGuideInTrainingDto.getPicture());
                if (userGuideInTrainingDto.getDescription() != null) user.setDescription(userGuideInTrainingDto.getDescription());
                repository.save(user);
            } else {
                System.out.println("User not found");
            }

        } else if (userEntity.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_HEAD_SECRETARY"))) {
            System.out.println("User is a HEAD SECRETARY");

        } else if (userEntity.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_DIERCTOR"))) {
            System.out.println("User is a DIRECTOR");

        } else if (userEntity.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_COORDINATOR"))) {
            UserCoordinatorDto userCoordinatorDto= objectMapper.convertValue(dtoMap, UserCoordinatorDto.class);
            Coordinator user = coordinatorService.getCoordinatorById(id);
            if (user != null) {
                if (userCoordinatorDto.getDepartment() != null) user.setDepartment(userCoordinatorDto.getDepartment());
                if (userCoordinatorDto.getGrade() != null) user.setGrade(userCoordinatorDto.getGrade());
                if (userCoordinatorDto.getFirstName() != null) user.setFirstName(userCoordinatorDto.getFirstName());
                if (userCoordinatorDto.getLastName() != null) user.setLastName(userCoordinatorDto.getLastName());
                if (userCoordinatorDto.getPhoneNumber() != null) user.setPhoneNumber(userCoordinatorDto.getPhoneNumber());
                if (userCoordinatorDto.getEmail() != null) user.setEmail(userCoordinatorDto.getEmail());
                if (userCoordinatorDto.getPicture() != null) user.setPicture(userCoordinatorDto.getPicture());
                if (userCoordinatorDto.getDescription() != null) user.setDescription(userCoordinatorDto.getDescription());
                repository.save(user);
            } else {
                System.out.println("User not found");
            }
        } else {
            System.out.println("User has no matching roles.");

        }

    }



    /*
    @Override
    public void changeRole(User user, Role role) {
        repository.delete(user);
        if(user instanceof Guide){
            user = (Guide) user;
        }
        if(user instanceof GuideInTraining){
            user = (GuideInTraining) user;
        }
        if(user instanceof Advisor){
            user = (Advisor) user;
        }
        User updatedUser;
        switch (role.toString()) {
            case ("ROLE_GUIDE"):
                updatedUser = new Guide();

                break;
        };
        default: throw new IllegalArgumentException("Unsupported role: " + role);
    }*/

    @Override
    public void deleteUserById(Long id) {
        User user = repository.findById(id).orElse(null);
        repository.delete(user);
    }

}
